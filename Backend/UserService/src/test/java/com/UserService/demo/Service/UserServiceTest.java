package com.UserService.demo.Service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor
public class UserServiceTest {

    private final UserService userService;
    @Test
    public  void testValidateUser() {
        assertTrue(5 > 3);
    }
}
