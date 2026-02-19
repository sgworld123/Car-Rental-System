package com.UserService.demo.Service;

import com.UserService.demo.Dto.ProfileDto;
import com.UserService.demo.Dto.RequestDto;
import com.UserService.demo.Dto.ResponseDto;
import com.UserService.demo.Model.User;
import com.UserService.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Boolean validateUser(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public ProfileDto returnProfile(String userId) {
        System.out.println(userId);
        User user = (User) userRepository.findByAuthId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ProfileDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }

}
