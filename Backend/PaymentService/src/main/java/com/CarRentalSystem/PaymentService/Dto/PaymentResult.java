package com.CarRentalSystem.PaymentService.Dto;

import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResult {
    private PaymentStatus status;
    private String transactionId;
    private String failureReason;
    private LocalDateTime timestamp;
}
