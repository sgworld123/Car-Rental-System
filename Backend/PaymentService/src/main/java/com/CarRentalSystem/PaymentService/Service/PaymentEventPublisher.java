package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Configs.PaymentRabbitMQConfig;
import com.CarRentalSystem.PaymentService.Dto.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    public void publishSuccess(PaymentSuccessEvent event)
    {
        try{
            rabbitTemplate.
                    convertAndSend(PaymentRabbitMQConfig.PAYMENT_EXCHANGE
                            , PaymentRabbitMQConfig.PAYMENT_SUCCESS_KEY,event);
            log.info("Published payment success event for bookingId: {}", event.getBookingId());
        }
        catch (Exception e)
        {
            log.error("Failed to publish payment success event for bookingId: {}. Error: {}", event.getBookingId(), e.getMessage());
        }
    }
    public void publishFailure(PaymentSuccessEvent event)
    {
        try{
            rabbitTemplate.
                    convertAndSend(PaymentRabbitMQConfig.PAYMENT_EXCHANGE
                            , PaymentRabbitMQConfig.PAYMENT_FAILED_KEY,event);
            log.info("Published payment failure event for bookingId: {}", event.getBookingId());
        }
        catch (Exception e)
        {
            log.error("Failed to publish payment failure event for bookingId: {}. Error: {}", event.getBookingId(), e.getMessage());
        }
    }
}
