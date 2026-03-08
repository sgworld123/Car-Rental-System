package com.CarRentalSystem.UserService.Controller;

import com.CarRentalSystem.UserService.Dto.LoginRequestDto;
import com.CarRentalSystem.UserService.Dto.LoginResponseDto;
import com.CarRentalSystem.UserService.Dto.SignupRequestDto;
import com.CarRentalSystem.UserService.Dto.SignupResponseDto;
import com.CarRentalSystem.UserService.Security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto)
    {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto)
    {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }
}
