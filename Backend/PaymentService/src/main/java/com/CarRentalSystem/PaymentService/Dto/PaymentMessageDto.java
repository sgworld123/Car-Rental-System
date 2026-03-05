package com.CarRentalSystem.PaymentService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMessageDto {
    private String bookingId;
    private String userId;
    private double amount;
    private BookingStatus bookingStatus;

}
