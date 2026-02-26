package com.CarRentalSystem.BookingService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String test() {
        redisTemplate.opsForValue().set("test-key", "hello");
        return redisTemplate.opsForValue().get("test-key");
    }
}