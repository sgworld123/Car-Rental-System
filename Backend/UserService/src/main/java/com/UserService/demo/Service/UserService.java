package com.UserService.demo.Service;

import com.UserService.demo.Dto.ProfileDto;
import com.UserService.demo.Exceptions.UserNotFoundException;
import com.UserService.demo.Model.User;
import com.UserService.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Boolean validateUser(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public ProfileDto returnProfile(String userId) {
        User user = userRepository.findByAuthId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return ProfileDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
}
