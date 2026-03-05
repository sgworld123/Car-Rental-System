package com.CarRentalSystem.PaymentService.Models;

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
@Document(collection = "payments")
public class Payment {
    private String paymentId;
    private String userId;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
