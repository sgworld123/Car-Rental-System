package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.PaymentMessageDto;
import com.CarRentalSystem.PaymentService.Dto.PaymentResult;
import com.CarRentalSystem.PaymentService.Dto.PaymentSuccessEvent;
import com.CarRentalSystem.PaymentService.Dto.RefundResult;
import com.CarRentalSystem.PaymentService.Errors.Exceptions;
import com.CarRentalSystem.PaymentService.Models.Payment;
import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import com.CarRentalSystem.PaymentService.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final MockRefundProvider mockRefundProvider;
    public Payment processPayment(PaymentMessageDto paymentMessageDto) {
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .userId(paymentMessageDto.getUserId())
                .amount(paymentMessageDto.getAmount())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        try{
            PaymentResult result = MockPaymentProvider.processPayment(paymentMessageDto.getAmount());
            if(result.getStatus() == PaymentStatus.SUCCESS) {
                paymentEventPublisher.publishSuccess(PaymentSuccessEvent.builder()
                        .bookingId(paymentMessageDto.getBookingId())
                        .amount(paymentMessageDto.getAmount())
                        .paidAt(LocalDateTime.now())
                        .build());
                payment.setStatus(PaymentStatus.SUCCESS);
            } else if(result.getStatus() == PaymentStatus.FAILURE) {
                paymentEventPublisher.publishFailure(PaymentSuccessEvent.builder()
                        .bookingId(paymentMessageDto.getBookingId())
                        .amount(paymentMessageDto.getAmount())
                        .paidAt(LocalDateTime.now())
                        .build());
                payment.setStatus(PaymentStatus.FAILURE);
            }
            else {
                payment.setStatus(PaymentStatus.UNKNOWN_ERROR);
            }
        }
        catch (Exceptions.PaymentProcessingException e) {
            log.error("Payment processing error: {}", e.getMessage());
            payment.setStatus(PaymentStatus.UNKNOWN_ERROR);
        }
        payment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
    public void initiateRefund(PaymentMessageDto paymentMessageDto)
    {
        try{
            RefundResult result = MockRefundProvider.processRefund(paymentMessageDto.getAmount(), paymentMessageDto.getBookingId());
            if(result.getStatus() == PaymentStatus.SUCCESS) {
                log.info("Refund successful for bookingId: {}", paymentMessageDto.getBookingId());
            } else if(result.getStatus() == PaymentStatus.FAILURE) {
                log.warn("Refund failed for bookingId: {}", paymentMessageDto.getBookingId());
            }
            else {
                log.error("Unknown error during refund for bookingId: {}", paymentMessageDto.getBookingId());
            }
        }
        catch (Exceptions.PaymentProcessingException e) {
            log.error("Refund processing error for bookingId: {}. Error: {}", paymentMessageDto.getBookingId(), e.getMessage());
        }
    }

}
