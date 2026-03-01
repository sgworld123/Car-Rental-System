package com.CarRentalSystem.BookingService.Repository;

import com.CarRentalSystem.BookingService.Models.BookedVehicleAndDates;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookedVehicleAndDatesRepository extends MongoRepository<BookedVehicleAndDates, String> {
    List<BookedVehicleAndDates> findByBookingId(String bookingId);

    List<BookedVehicleAndDates> findByDateBefore(LocalDate now);

    void deleteByBookingId(String bookingId);
}
