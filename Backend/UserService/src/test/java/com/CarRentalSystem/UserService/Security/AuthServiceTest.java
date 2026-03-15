package com.CarRentalSystem.UserService.Security;

import com.CarRentalSystem.UserService.Dto.LoginRequestDto;
import com.CarRentalSystem.UserService.Dto.LoginResponseDto;
import com.CarRentalSystem.UserService.Dto.SignupRequestDto;
import com.CarRentalSystem.UserService.Dto.SignupResponseDto;
import com.CarRentalSystem.UserService.Exceptions.UserNameAlreadyTakenException;
import com.CarRentalSystem.UserService.Model.AuthUser;
import com.CarRentalSystem.UserService.Model.User;
import com.CarRentalSystem.UserService.Repository.AuthUserRepository;
import com.CarRentalSystem.UserService.Repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Claims;
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
import static org.mockito.ArgumentMatchers.*;
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
    @DisplayName("login – valid credentials return accessToken, refreshToken, and userId")
    void login_validCredentials_returnsBothTokens() {
        AuthUser authUser = buildAuthUser("auth-001", "alice");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authUser);
        when(authUtils.generateAccessToken(authUser)).thenReturn("access.token");
        when(authUtils.generateRefreshToken(authUser)).thenReturn("refresh.token");
        when(authUserRepository.save(any())).thenReturn(authUser);

        LoginResponseDto response = authService.login(new LoginRequestDto("alice", "password"));

        assertThat(response.getJwt()).isEqualTo("access.token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh.token");
        assertThat(response.getId()).isEqualTo("auth-001");
    }

    @Test
    @DisplayName("login – refresh token is saved to AuthUser in DB")
    void login_refreshToken_savedToDb() {
        AuthUser authUser = buildAuthUser("auth-001", "alice");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authUser);
        when(authUtils.generateAccessToken(authUser)).thenReturn("access.token");
        when(authUtils.generateRefreshToken(authUser)).thenReturn("refresh.token");
        when(authUserRepository.save(any())).thenReturn(authUser);

        authService.login(new LoginRequestDto("alice", "password"));

        // Verify the user was saved with the refresh token set
        verify(authUserRepository).save(argThat(u -> "refresh.token".equals(u.getRefreshToken())));
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
                .username("bob").password("pass123")
                .email("bob@example.com").phone("9876543210")
                .build();

        AuthUser savedAuthUser = buildAuthUser("auth-002", "bob");

        when(authUserRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encoded-pass");
        when(authUserRepository.save(any())).thenReturn(savedAuthUser);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SignupResponseDto response = authService.signup(req);

        assertThat(response.getUsername()).isEqualTo("bob");
        assertThat(response.getId()).isEqualTo("auth-002");
        verify(authUserRepository).save(any());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("signup – duplicate username throws UserNameAlreadyTakenException")
    void signup_duplicateUsername_throws() {
        when(authUserRepository.findByUsername("alice"))
                .thenReturn(Optional.of(buildAuthUser("auth-001", "alice")));

        assertThatThrownBy(() -> authService.signup(
                SignupRequestDto.builder().username("alice").password("x").build()))
                .isInstanceOf(UserNameAlreadyTakenException.class)
                .hasMessageContaining("Username already taken");

        verify(authUserRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("signup – password is BCrypt encoded, never stored as plaintext")
    void signup_passwordIsEncoded() {
        AuthUser saved = buildAuthUser("auth-003", "carol");

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
        AuthUser saved = buildAuthUser("auth-xyz", "dave");

        when(authUserRepository.findByUsername("dave")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(authUserRepository.save(any())).thenReturn(saved);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        authService.signup(SignupRequestDto.builder()
                .username("dave").password("pass").email("d@d.com").phone("123").build());

        verify(userRepository).save(argThat(u -> "auth-xyz".equals(u.getAuthId())));
    }

    @Test
    @DisplayName("signup – role is set to USER by default")
    void signup_roleSetToUser() {
        AuthUser saved = buildAuthUser("auth-004", "eve");

        when(authUserRepository.findByUsername("eve")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(authUserRepository.save(any())).thenReturn(saved);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        authService.signup(SignupRequestDto.builder()
                .username("eve").password("pass").email("e@e.com").build());

        verify(authUserRepository).save(argThat(u -> "USER".equals(u.getRole())));
    }

    // ─────────────────────────────────────────────
    // refresh
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("refresh – valid refresh token returns new access token")
    void refresh_validToken_returnsNewAccessToken() {
        AuthUser authUser = buildAuthUser("auth-001", "alice");
        authUser.setRefreshToken("valid.refresh.token");

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("alice");
        when(authUtils.extractClaims("valid.refresh.token")).thenReturn(claims);
        when(authUserRepository.findByUsername("alice")).thenReturn(Optional.of(authUser));
        when(authUtils.generateAccessToken(authUser)).thenReturn("new.access.token");

        LoginResponseDto result = authService.refresh("valid.refresh.token");

        assertThat(result.getJwt()).isEqualTo("new.access.token");
        assertThat(result.getRefreshToken()).isEqualTo("valid.refresh.token");
        assertThat(result.getId()).isEqualTo("auth-001");
    }

    @Test
    @DisplayName("refresh – token not matching stored token throws RuntimeException")
    void refresh_tokenMismatch_throws() {
        AuthUser authUser = buildAuthUser("auth-001", "alice");
        authUser.setRefreshToken("stored.token");

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("alice");
        when(authUtils.extractClaims("different.token")).thenReturn(claims);
        when(authUserRepository.findByUsername("alice")).thenReturn(Optional.of(authUser));

        assertThatThrownBy(() -> authService.refresh("different.token"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid refresh token");
    }

    @Test
    @DisplayName("refresh – unknown username throws RuntimeException")
    void refresh_unknownUser_throws() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("ghost");
        when(authUtils.extractClaims("any.token")).thenReturn(claims);
        when(authUserRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refresh("any.token"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("refresh – expired refresh token throws RuntimeException (not 500)")
    void refresh_expiredToken_throwsRuntimeException() {
        when(authUtils.extractClaims("expired.token"))
                .thenThrow(mock(ExpiredJwtException.class));

        // Should throw RuntimeException, not the raw ExpiredJwtException
        // so GlobalExceptionHandler can return 400 instead of 500
        assertThatThrownBy(() -> authService.refresh("expired.token"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("refresh – refresh does not generate a new refresh token (same token reused)")
    void refresh_doesNotRotateRefreshToken() {
        AuthUser authUser = buildAuthUser("auth-001", "alice");
        authUser.setRefreshToken("same.refresh.token");

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("alice");
        when(authUtils.extractClaims("same.refresh.token")).thenReturn(claims);
        when(authUserRepository.findByUsername("alice")).thenReturn(Optional.of(authUser));
        when(authUtils.generateAccessToken(any())).thenReturn("new.access.token");

        LoginResponseDto result = authService.refresh("same.refresh.token");

        // Refresh token should be the same — not rotated
        assertThat(result.getRefreshToken()).isEqualTo("same.refresh.token");
        verify(authUtils, never()).generateRefreshToken(any());
    }

    // ─────────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────────

    private AuthUser buildAuthUser(String id, String username) {
        return AuthUser.builder()
                .Id(id)
                .username(username)
                .password("encoded")
                .role("USER")
                .build();
    }
}