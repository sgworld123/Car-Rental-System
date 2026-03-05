package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.PaymentMessageDto;
import com.CarRentalSystem.PaymentService.Dto.PaymentResult;
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
    public Payment processPayment(PaymentMessageDto paymentMessageDto) {
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .userId(paymentMessageDto.getUserId())
                .amount(paymentMessageDto.getAmount())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);
        try{
            PaymentResult result = MockPaymentProvider.processPayment(paymentMessageDto.getAmount());
            if(result.getStatus() == PaymentStatus.SUCCESS) {
                payment.setStatus(PaymentStatus.SUCCESS);
            } else if(result.getStatus() == PaymentStatus.FAILURE) {
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

}
