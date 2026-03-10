package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Configs.PaymentRabbitMQConfig;
import com.CarRentalSystem.PaymentService.Dto.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundEventPublisher {
    public final RabbitTemplate rabbitTemplate;
    public void publishRefundResult(RefundStatus refundStatus) {
        try {
            rabbitTemplate.convertAndSend(PaymentRabbitMQConfig.PAYMENT_EXCHANGE, PaymentRabbitMQConfig.PAYMENT_REFUND_KEY,
                    refundStatus);
            log.info("Published refund result for bookingId: {}", refundStatus.getBookingId());
        } catch (Exception e) {
            log.error("Failed to publish refund result for bookingId: {}. Error: {}", refundStatus.getBookingId(), e.getMessage());
        }
    }
}
