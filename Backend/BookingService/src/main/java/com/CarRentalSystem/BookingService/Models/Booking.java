package com.CarRentalSystem.BookingService.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "BookingData")
public class Booking {
    @Id
    private String id;
    private String bookingId;
    private String agencyId;
    private String userId;
    private String imageUrl;
    private String carName;
    private String vehicleId;
    private double cost;
    private LocalDate fromDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private BookingStatus status;
}
