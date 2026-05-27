package com.CarRentalSystem.UserService.Service;

import com.CarRentalSystem.UserService.Dto.UserCreatedEventDto;
import com.CarRentalSystem.UserService.Model.User;
import com.CarRentalSystem.UserService.Repository.UserRepository;
import com.CarRentalSystem.UserService.Dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.rmi.server.LogStream.log;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Boolean validateUser(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public ProfileDto returnProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ProfileDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
    public void createUser(UserCreatedEventDto userCreatedEventDto)
    {
        User user = User.builder()
                .name(userCreatedEventDto.getUserName())
                .email(userCreatedEventDto.getUserEmail())
                .phone(userCreatedEventDto.getPhoneNumber())
                .build();
        userRepository.save(user);
        log("Created user with id " +user.getId());
    }
}
