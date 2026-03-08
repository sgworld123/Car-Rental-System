package com.CarRentalSystem.UserService.Security;

import com.CarRentalSystem.UserService.Dto.LoginRequestDto;
import com.CarRentalSystem.UserService.Dto.LoginResponseDto;
import com.CarRentalSystem.UserService.Dto.SignupRequestDto;
import com.CarRentalSystem.UserService.Dto.SignupResponseDto;
import com.CarRentalSystem.UserService.Model.AuthUser;
import com.CarRentalSystem.UserService.Model.User;
import com.CarRentalSystem.UserService.Repository.AuthUserRepository;
import com.CarRentalSystem.UserService.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private AuthUtils authUtils;
    @Mock private AuthUserRepository authUserRepository;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private Authentication authentication;

    @InjectMocks private AuthService authService;

    // ─────────────────────────────────────────────
    // login
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("login – valid credentials return JWT and user ID")
    void login_validCredentials_returnsToken() {
        AuthUser authUser = AuthUser.builder()
                .Id("auth-001")
                .username("alice")
                .password("encoded")
                .role("USER")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authUser);
        when(authUtils.generateAccessToken(authUser)).thenReturn("mocked.jwt.token");

        LoginResponseDto response = authService.login(new LoginRequestDto("alice", "password"));

        assertThat(response.getJwt()).isEqualTo("mocked.jwt.token");
        assertThat(response.getId()).isEqualTo("auth-001");
    }

    @Test
    @DisplayName("login – bad credentials throw BadCredentialsException")
    void login_badCredentials_throws() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(new LoginRequestDto("alice", "wrong")))
                .isInstanceOf(BadCredentialsException.class);
    }

    // ─────────────────────────────────────────────
    // signup
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("signup – new user creates AuthUser and User records")
    void signup_newUser_success() {
        SignupRequestDto req = SignupRequestDto.builder()
                .username("bob")
                .password("pass123")
                .email("bob@example.com")
                .phone("9876543210")
                .build();

        AuthUser savedAuthUser = AuthUser.builder()
                .Id("auth-002")
                .username("bob")
                .password("encoded-pass")
                .role("USER")
                .build();

        when(authUserRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encoded-pass");
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(savedAuthUser);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        SignupResponseDto response = authService.signup(req);

        assertThat(response.getUsername()).isEqualTo("bob");
        assertThat(response.getId()).isEqualTo("auth-002");
        verify(authUserRepository).save(any());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("signup – duplicate username throws RuntimeException")
    void signup_duplicateUsername_throws() {
        when(authUserRepository.findByUsername("alice"))
                .thenReturn(Optional.of(AuthUser.builder().username("alice").build()));

        assertThatThrownBy(() -> authService.signup(
                SignupRequestDto.builder().username("alice").password("x").build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already taken");

        verify(authUserRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("signup – password is encoded, never stored as plaintext")
    void signup_passwordIsEncoded_notPlaintext() {
        AuthUser saved = AuthUser.builder().Id("auth-003").username("carol").build();

        when(authUserRepository.findByUsername("carol")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plaintext")).thenReturn("hashed-pass");
        when(authUserRepository.save(any())).thenReturn(saved);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        authService.signup(SignupRequestDto.builder()
                .username("carol").password("plaintext").email("c@c.com").build());

        verify(passwordEncoder).encode("plaintext");
        verify(authUserRepository).save(argThat(u -> "hashed-pass".equals(u.getPassword())));
    }

    @Test
    @DisplayName("signup – User profile is linked to AuthUser via authId")
    void signup_userLinkedToAuthUser() {
        AuthUser saved = AuthUser.builder().Id("auth-xyz").username("dave").build();

        when(authUserRepository.findByUsername("dave")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(authUserRepository.save(any())).thenReturn(saved);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        authService.signup(SignupRequestDto.builder()
                .username("dave").password("pass").email("dave@d.com").phone("123").build());

        // The User's authId must match the saved AuthUser's ID
        verify(userRepository).save(argThat(u -> "auth-xyz".equals(u.getAuthId())));
    }
}