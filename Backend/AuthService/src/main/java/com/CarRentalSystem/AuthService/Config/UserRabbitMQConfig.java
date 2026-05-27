package com.CarRentalSystem.AuthService.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitMQConfig {
    public static final String USER_EXCHANGE_NAME = "user.exchange";
    public static final String USER_CREATED_ROUTING_KEY = "user.routing.key";
    @Bean
    public TopicExchange userExchange(){
        return new TopicExchange(USER_EXCHANGE_NAME);
    }
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
