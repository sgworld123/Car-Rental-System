package com.CarRentalSystem.PaymentService.Configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingRabbitMQConfig {
    public static final String BOOKING_EXCHANGE = "booking.exchange";
    public static final String BOOKING_CREATED_QUEUE = "booking.created.queue";
    public static final String BOOKING_CANCELLED_QUEUE = "booking.cancelled.queue";
    public static final String BOOKING_CREATED_KEY = "booking.created.key";
    public static final String BOOKING_CANCELLED_KEY = "booking.cancelled.key";
    @Bean
    public Queue bookingCreatedQueue(){
        return QueueBuilder.durable(BOOKING_CREATED_QUEUE).build();
    }
    @Bean
    public Queue bookingCancelledQueue(){
        return QueueBuilder.durable(BOOKING_CANCELLED_QUEUE).build();
    }
    @Bean
    public TopicExchange bookingExchange(){
        return new TopicExchange(BOOKING_EXCHANGE);
    }
    @Bean
    public Binding bookingCreatedBinding(){
        return BindingBuilder
                .bind(bookingCreatedQueue())
                .to(bookingExchange())
                .with(BOOKING_CREATED_KEY);
    }
    @Bean
    public Binding bookingCancelledBinding(){
        return BindingBuilder
                .bind(bookingCancelledQueue())
                .to(bookingExchange())
                .with(BOOKING_CANCELLED_KEY);
    }
}
