package com.UserService.demo.Service;

import com.UserService.demo.Dto.RequestDto;
import com.UserService.demo.Dto.ResponseDto;
import com.UserService.demo.Model.User;
import com.UserService.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;

    //register user
    public ResponseDto registerUser(RequestDto requestDto)
    {
        User user  = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();
        userRepository.save(user);
        return ResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail()).build();
    }
}
