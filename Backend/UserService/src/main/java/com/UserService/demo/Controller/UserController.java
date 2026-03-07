package com.UserService.demo.Controller;

import com.UserService.demo.Dto.ProfileDto;
import com.UserService.demo.Dto.RequestDto;
import com.UserService.demo.Dto.ResponseDto;
import com.UserService.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/validate/{userId}")
    public Boolean validateUser(@PathVariable String userId)
    {
        return userService.validateUser(userId);
    }
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> returnProfile(@RequestHeader("X-User-Id") String userId)
    {
        return ResponseEntity.ok(userService.returnProfile(userId));
    }
}
