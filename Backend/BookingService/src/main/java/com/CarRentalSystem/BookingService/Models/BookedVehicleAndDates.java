package com.CarRentalSystem.BookingService.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@CompoundIndex(name = "vehicle_date_unique", def = "{'vehicleId': 1, 'date': 1}", unique = true)
@Document(collection = "BookedVehicleAndDates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookedVehicleAndDates {
    private String bookingId;
    private String vehicleId;
    private LocalDate date;

}
