package com.CarRentalSystem.UserService.Service;

import com.CarRentalSystem.UserService.Config.UserRabbitMQConfig;
import com.CarRentalSystem.UserService.Dto.UserCreatedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserEventConsumer {
    private final UserService userService;
    @RabbitListener(queues = UserRabbitMQConfig.USER_QUEUE_NAME)
    public void consume(UserCreatedEventDto event)
    {
        userService.createUser(event);
    }
}
