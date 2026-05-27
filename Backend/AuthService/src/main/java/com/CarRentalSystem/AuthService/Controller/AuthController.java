package com.CarRentalSystem.AuthService.Controller;

import com.CarRentalSystem.AuthService.Dto.LoginRequestDto;
import com.CarRentalSystem.AuthService.Dto.LoginResponseDto;
import com.CarRentalSystem.AuthService.Dto.SignupRequestDto;
import com.CarRentalSystem.AuthService.Dto.SignupResponseDto;
import com.CarRentalSystem.AuthService.Security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto)
    {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto)
    {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(authService.refresh(body.get("refreshToken")));
    }
}
