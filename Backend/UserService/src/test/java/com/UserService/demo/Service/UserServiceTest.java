package com.UserService.demo.Service;

import com.UserService.demo.Dto.ProfileDto;
import com.UserService.demo.Model.User;
import com.UserService.demo.Repository.UserRepository;
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // ─────────────────────────────────────────────
    // validateUser
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validateUser – returns true when user exists by ID")
    void validateUser_exists_returnsTrue() {
        when(userRepository.findById("user-1"))
                .thenReturn(Optional.of(User.builder().id("user-1").build()));

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
    @DisplayName("returnProfile – returns correct profile fields for valid authId")
    void returnProfile_valid_returnsProfile() {
        User user = User.builder()
                .authId("auth-001")
                .name("Alice")
                .email("alice@example.com")
                .phone("9876543210")
                .build();

        when(userRepository.findByAuthId("auth-001")).thenReturn(Optional.of(user));

        ProfileDto profile = userService.returnProfile("auth-001");

        assertThat(profile.getName()).isEqualTo("Alice");
        assertThat(profile.getEmail()).isEqualTo("alice@example.com");
        assertThat(profile.getPhone()).isEqualTo("9876543210");
    }

    @Test
    @DisplayName("returnProfile – throws RuntimeException when authId not found")
    void returnProfile_notFound_throws() {
        when(userRepository.findByAuthId("bad-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.returnProfile("bad-id"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("returnProfile – profile fields are correctly mapped from User model")
    void returnProfile_fieldMapping_noCrossContamination() {
        User user = User.builder()
                .authId("auth-002")
                .name("Bob")
                .email("bob@test.com")
                .phone("1234567890")
                .build();

        when(userRepository.findByAuthId("auth-002")).thenReturn(Optional.of(user));

        ProfileDto profile = userService.returnProfile("auth-002");

        assertThat(profile.getName()).isEqualTo("Bob");
        assertThat(profile.getEmail()).doesNotContain("alice");
    }
}