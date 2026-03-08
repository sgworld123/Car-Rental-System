package com.CarRentalSystem.UserService.Repository;

import com.CarRentalSystem.UserService.Model.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends MongoRepository<AuthUser,String> {

    Optional<AuthUser> findByUsername(String username);
}
