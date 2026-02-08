package com.UserService.demo.Security;

import com.UserService.demo.Dto.LoginRequestDto;
import com.UserService.demo.Dto.LoginResponseDto;
import com.UserService.demo.Dto.SignupResponseDto;
import com.UserService.demo.Model.AuthUser;
import com.UserService.demo.Model.User;
import com.UserService.demo.Repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final AuthUserRepository authUserRepository;
    public final PasswordEncoder passwordEncoder;
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

    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {
        AuthUser authUser = authUserRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if(authUser != null) throw new IllegalArgumentException("user already existes");

        authUser = authUserRepository.save(AuthUser.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role("USER")
                .build());

        return SignupResponseDto.builder()
                .username(authUser.getUsername())
                .id(authUser.getId())
                .build();
    }
}
