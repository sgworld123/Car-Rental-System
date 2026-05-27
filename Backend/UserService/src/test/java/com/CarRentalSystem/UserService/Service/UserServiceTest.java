package com.CarRentalSystem.UserService.Service;

import com.CarRentalSystem.UserService.Dto.ProfileDto;
import com.CarRentalSystem.UserService.Model.User;
import com.CarRentalSystem.UserService.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private UserService userService;

    // ─────────────────────────────────────────────
    // validateUser
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validateUser – returns true when user exists")
    void validateUser_exists_returnsTrue() {
        User user = User.builder().id("user-1").authId("auth-1").build();
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        assertThat(userService.validateUser("user-1")).isTrue();
    }

    @Test
    @DisplayName("validateUser – returns false when user does not exist")
    void validateUser_notExists_returnsFalse() {
        when(userRepository.findById("ghost")).thenReturn(Optional.empty());

        assertThat(userService.validateUser("ghost")).isFalse();
    }

    // ─────────────────────────────────────────────
    // returnProfile
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("returnProfile – returns correct profile DTO for existing user")
    void returnProfile_found_returnsProfileDto() {
        User user = User.builder()
                .id("user-1")
                .authId("auth-1")
                .name("Vibhu")
                .email("vibhu@example.com")
                .phone("9876543210")
                .build();

        when(userRepository.findByAuthId("auth-1")).thenReturn(Optional.of(user));

        ProfileDto result = userService.returnProfile("auth-1");

        assertThat(result.getName()).isEqualTo("Vibhu");
        assertThat(result.getEmail()).isEqualTo("vibhu@example.com");
        assertThat(result.getPhone()).isEqualTo("9876543210");
    }

    @Test
    @DisplayName("returnProfile – all fields correctly mapped from User to ProfileDto")
    void returnProfile_allFieldsMapped() {
        User user = User.builder()
                .authId("auth-2")
                .name("Alice")
                .email("alice@test.com")
                .phone("1234567890")
                .build();

        when(userRepository.findByAuthId("auth-2")).thenReturn(Optional.of(user));

        ProfileDto result = userService.returnProfile("auth-2");

        // Verify no field is null (all 3 profile fields present)
        assertThat(result.getName()).isNotNull();
        assertThat(result.getEmail()).isNotNull();
        assertThat(result.getPhone()).isNotNull();
    }


    @Test
    @DisplayName("returnProfile – does not call findById, only findByAuthId")
    void returnProfile_usesAuthId_notId() {
        User user = User.builder().authId("auth-3").name("Bob").email("bob@b.com").phone("111").build();
        when(userRepository.findByAuthId("auth-3")).thenReturn(Optional.of(user));

        userService.returnProfile("auth-3");

        verify(userRepository).findByAuthId("auth-3");
        verify(userRepository, never()).findById(any());
    }
}