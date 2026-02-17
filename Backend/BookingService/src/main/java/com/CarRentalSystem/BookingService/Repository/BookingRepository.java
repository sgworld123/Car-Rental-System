package com.CarRentalSystem.BookingService.Repository;

import com.CarRentalSystem.BookingService.Models.Booking;
import com.CarRentalSystem.BookingService.Models.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    Optional<Booking> findByBookingId(String bookingId);

    List<Booking> findByStatusAndEndDateBefore(
            BookingStatus status,
            LocalDate date
    );

    List<Optional<Object>> findByUserId(String userId);
    boolean existsByVehicleIdAndStatusAndFromDateLessThanEqualAndEndDateGreaterThanEqual(
            String vehicleId,
            BookingStatus status,
            LocalDate date1,
            LocalDate date2
    );
}

