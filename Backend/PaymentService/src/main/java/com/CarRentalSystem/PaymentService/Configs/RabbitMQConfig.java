package com.CarRentalSystem.PaymentService.Configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange — the entry point, events are published here
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // Queues — BookingService listens to these
    public static final String PAYMENT_SUCCESS_QUEUE = "payment.success.queue";
    public static final String PAYMENT_FAILED_QUEUE  = "payment.failed.queue";

    // Routing keys — how exchange knows which queue to route to
    public static final String PAYMENT_SUCCESS_KEY = "payment.success";
    public static final String PAYMENT_FAILED_KEY  = "payment.failed";

    // Dead Letter setup — for failed message handling
    public static final String PAYMENT_DLQ = "payment.dead.letter.queue";
    public static final String PAYMENT_DLX = "payment.dead.letter.exchange";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentSuccessQueue() {
        return QueueBuilder.durable(PAYMENT_SUCCESS_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX)
                .withArgument("x-dead-letter-routing-key", "payment.dead")
                .build();
    }

    @Bean
    public Queue paymentFailedQueue() {
        return QueueBuilder.durable(PAYMENT_FAILED_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX)
                .withArgument("x-dead-letter-routing-key", "payment.dead")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(PAYMENT_DLQ).build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(PAYMENT_DLX);
    }

    // Bindings — wire queues to exchange via routing keys
    @Bean
    public Binding successBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(PAYMENT_SUCCESS_KEY);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder
                .bind(paymentFailedQueue())
                .to(paymentExchange())
                .with(PAYMENT_FAILED_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("payment.dead");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
