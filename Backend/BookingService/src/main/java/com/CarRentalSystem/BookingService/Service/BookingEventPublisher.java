package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Config.BookingRabbitMQConfig;
import com.CarRentalSystem.BookingService.Dto.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void handleBookingCreated(BookingCreatedEvent event) {
        try{
            rabbitTemplate.convertAndSend(BookingRabbitMQConfig.BOOKING_EXCHANGE
                    , BookingRabbitMQConfig.BOOKING_CREATED_KEY
                    , event);
            log.info("Published booking created event for bookingId: {}", event.getBookingId());
        }
        catch (Exception e){
            log.error("Failed to publish booking created event for bookingId: {}. Error: {}", event.getBookingId(), e.getMessage());
        }
    }
    public void handleBookingCancelled(String bookingId) {
        try{
            rabbitTemplate.convertAndSend(BookingRabbitMQConfig.BOOKING_EXCHANGE
                    , BookingRabbitMQConfig.BOOKING_CANCELLED_KEY
                    , bookingId);
            log.info("Published booking cancelled event for bookingId: {}", bookingId);
        }
        catch (Exception e){
            log.error("Failed to publish booking cancelled event for bookingId: {}. Error: {}", bookingId, e.getMessage());
        }
    }
}
