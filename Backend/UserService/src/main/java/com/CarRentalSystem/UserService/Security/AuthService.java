package com.CarRentalSystem.UserService.Security;

import com.CarRentalSystem.UserService.Exceptions.UserNameAlreadyTakenException;
import com.CarRentalSystem.UserService.Repository.AuthUserRepository;
import com.CarRentalSystem.UserService.Repository.UserRepository;
import com.CarRentalSystem.UserService.Dto.LoginRequestDto;
import com.CarRentalSystem.UserService.Dto.LoginResponseDto;
import com.CarRentalSystem.UserService.Dto.SignupRequestDto;
import com.CarRentalSystem.UserService.Dto.SignupResponseDto;
import com.CarRentalSystem.UserService.Model.AuthUser;
import com.CarRentalSystem.UserService.Model.User;
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
                    throw new UserNameAlreadyTakenException("Username already taken");
                });

        AuthUser authUser = AuthUser.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role("USER")
                .build();
        authUser = authUserRepository.save(authUser);

        log.info("Created AuthUser with ID: {}", authUser.getId());

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
