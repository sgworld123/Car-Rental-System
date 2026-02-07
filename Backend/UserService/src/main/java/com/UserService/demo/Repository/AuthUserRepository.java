package com.UserService.demo.Repository;

import com.UserService.demo.Model.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends MongoRepository<AuthUser,String> {

    Optional<AuthUser> findByUsername(String username);
}
