package com.CarRentalSystem.UserService.Controller;

import com.CarRentalSystem.UserService.Dto.ProfileDto;
import com.CarRentalSystem.UserService.Service.UserService;
import lombok.RequiredArgsConstructor;
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
