package com.UserService.demo.Controller;

import com.UserService.demo.Dto.RequestDto;
import com.UserService.demo.Dto.ResponseDto;
import com.UserService.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseDto registerUser(@RequestBody RequestDto requestDto)
    {
        return userService.registerUser(requestDto);
    }
    @GetMapping("/validate/{userId}")
    public Boolean validateUser(@PathVariable String userId)
    {
        return userService.validateUser(userId);
    }
}
