package com.UserService.demo.Security;

import com.UserService.demo.Dto.LoginRequestDto;
import com.UserService.demo.Dto.LoginResponseDto;
import com.UserService.demo.Dto.SignupRequestDto;
import com.UserService.demo.Dto.SignupResponseDto;
import com.UserService.demo.Model.AuthUser;
import com.UserService.demo.Model.User;
import com.UserService.demo.Repository.AuthUserRepository;
import com.UserService.demo.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final AuthUserRepository authUserRepository;
    public final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        String token = authUtils.generateAccessToken(authUser);

        return LoginResponseDto.builder()
                .jwt(token)
                .id(authUser.getId())
                .build();
    }

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        authUserRepository.findByUsername(signupRequestDto.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("Username already taken");
                });

        AuthUser authUser = AuthUser.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role("USER")
                .build();
        authUser = authUserRepository.save(authUser);

        System.out.println(authUser.getId());

        User user = User.builder()
                .authId(authUser.getId())
                .name(signupRequestDto.getUsername())
                .phone(signupRequestDto.getPhone())
                .email(signupRequestDto.getEmail())
                .build();

        userRepository.save(user);

        return SignupResponseDto.builder()
                .id(authUser.getId())
                .username(authUser.getUsername())
                .build();
    }

}
