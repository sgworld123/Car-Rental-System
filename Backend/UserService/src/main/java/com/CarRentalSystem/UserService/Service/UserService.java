package com.CarRentalSystem.UserService.Service;

import com.CarRentalSystem.UserService.Exceptions.UserNotFoundException;
import com.CarRentalSystem.UserService.Model.User;
import com.CarRentalSystem.UserService.Repository.UserRepository;
import com.CarRentalSystem.UserService.Dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
