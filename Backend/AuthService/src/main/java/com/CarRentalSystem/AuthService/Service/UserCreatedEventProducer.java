package com.CarRentalSystem.AuthService.Service;

import com.CarRentalSystem.AuthService.Config.UserRabbitMQConfig;
import com.CarRentalSystem.AuthService.Dto.UserCreatedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreatedEventProducer {
    private final RabbitTemplate rabbitTemplate;
    public void sendUserCreatedEvent(UserCreatedEventDto userCreatedEventDto){
        try{
            rabbitTemplate.convertAndSend(UserRabbitMQConfig.USER_EXCHANGE_NAME,
                    UserRabbitMQConfig.USER_CREATED_ROUTING_KEY,userCreatedEventDto);
            log.info("UserCreatedEventProducer sent userCreatedEvent");
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
