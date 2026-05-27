package com.CarRentalSystem.AuthService.Repository;

import com.CarRentalSystem.AuthService.Model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthUser, String> {
    Optional<AuthUser> findByUsername(String username);
}
