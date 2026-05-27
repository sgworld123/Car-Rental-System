package com.CarRentalSystem.UserService.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitMQConfig {
    public static final String USER_EXCHANGE_NAME = "user.exchange";
    public static final String USER_QUEUE_NAME = "user.queue";
    public static final String USER_ROUTING_KEY = "user.routing.key";
    @Bean
    public TopicExchange userExchange(){
        return new TopicExchange(USER_EXCHANGE_NAME);
    }
    @Bean
    public Queue userQueue(){
        return QueueBuilder.durable(USER_QUEUE_NAME).build();
    }
    @Bean
    public Binding userBinding(){
        return BindingBuilder.bind(userQueue()).to(userExchange()).with(USER_ROUTING_KEY);
    }
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
