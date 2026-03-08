package com.CarRentalSystem.UserService.Repository;

import com.CarRentalSystem.UserService.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByAuthId(String userId);
}
