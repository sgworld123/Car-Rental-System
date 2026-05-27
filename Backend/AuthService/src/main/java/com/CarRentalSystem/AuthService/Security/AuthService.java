package com.CarRentalSystem.AuthService.Security;

import com.CarRentalSystem.AuthService.Dto.*;

import com.CarRentalSystem.AuthService.Exceptions.ExpiredJwtException;
import com.CarRentalSystem.AuthService.Exceptions.UserNameAlreadyTakenException;
import com.CarRentalSystem.AuthService.Exceptions.UserNotFoundException;
import com.CarRentalSystem.AuthService.Model.AuthUser;
import com.CarRentalSystem.AuthService.Model.RoleType;
import com.CarRentalSystem.AuthService.Repository.AuthRepository;
import com.CarRentalSystem.AuthService.Service.UserCreatedEventProducer;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final UserCreatedEventProducer userCreatedEventProducer;
    private final AuthRepository authRepository;
    public final PasswordEncoder passwordEncoder;
    public LoginResponseDto login(@Valid LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername()
                        ,loginRequestDto.getPassword())
        );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        String token = authUtils.generateAccessToken(authUser);
        String refreshToken = authUtils.generateRefreshToken(authUser);

        authUser.setRefreshToken(refreshToken);
        authRepository.save(authUser);

        return LoginResponseDto.builder()
                .jwt(token)
                .refreshToken(refreshToken)
                .id(authUser.getId())
                .build();
    }

    @Transactional
    public SignupResponseDto signup(@Valid SignupRequestDto signupRequestDto) {

        authRepository.findByUsername(signupRequestDto.getUsername())
                .ifPresent(user -> {
                    throw new UserNameAlreadyTakenException("Username already taken");
                });

        AuthUser authUser = AuthUser.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role(RoleType.ROLE_USER)
                .build();
        authUser = authRepository.save(authUser);

        log.info("Created AuthUser with ID: {}", authUser.getId());

        userCreatedEventProducer.sendUserCreatedEvent(UserCreatedEventDto.builder()
                .userName(signupRequestDto.getUsername())
                .userEmail(signupRequestDto.getEmail())
                .phoneNumber(signupRequestDto.getPhone())
                .build());

        return SignupResponseDto.builder()
                .id(String.valueOf(authUser.getId()))
                .username(authUser.getUsername())
                .build();
    }
    public LoginResponseDto refresh(String refreshToken) {
        // Validate the token
        Claims claims = authUtils.extractClaims(refreshToken);
        String username = claims.getSubject();

        AuthUser authUser = authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!refreshToken.equals(authUser.getRefreshToken())) {
            throw new ExpiredJwtException("Invalid refresh token");
        }
        String newAccessToken = authUtils.generateAccessToken(authUser);

        return LoginResponseDto.builder()
                .jwt(newAccessToken)
                .refreshToken(refreshToken)
                .id(authUser.getId())
                .build();
    }

}
