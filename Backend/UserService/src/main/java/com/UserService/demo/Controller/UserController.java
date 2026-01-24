package com.UserService.demo.Controller;

import com.UserService.demo.Dto.RequestDto;
import com.UserService.demo.Dto.ResponseDto;
import com.UserService.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/register")
    public ResponseDto registerUser(@RequestBody RequestDto requestDto)
    {
        return userService.registerUser(requestDto);
    }
}
