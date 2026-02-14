package com.UserService.demo.Controller;

import com.UserService.demo.Dto.LoginRequestDto;
import com.UserService.demo.Dto.LoginResponseDto;
import com.UserService.demo.Dto.SignupResponseDto;
import com.UserService.demo.Security.AuthService;
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
    public ResponseEntity<SignupResponseDto> signup(@RequestBody LoginRequestDto loginRequestDto)
    {
        return ResponseEntity.ok(authService.signup(loginRequestDto));
    }
//    @GetMapping("/getUidFromToken")
//    public ResponseEntity<String> getUidFromToken(@RequestHeader("Authorization") String token)
//    {
//        return ResponseEntity.ok(authService.getUserIdFromToken(token.substring(7)));
    }
}
