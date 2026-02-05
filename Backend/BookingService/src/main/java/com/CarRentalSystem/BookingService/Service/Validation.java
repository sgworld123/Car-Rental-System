package com.CarRentalSystem.BookingService.Service;

import com.CarRentalSystem.BookingService.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class Validation {
    private final WebClient userWebClient;
    private final WebClient agencyWebClient;
    private final BookingRepository bookingRepository;
    private final RedisTemplate redisTemplate;

    public boolean isUserValid(String userId) {
        Boolean isValid = userWebClient.get()
                .uri("/user/validate/{userId}", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return isValid != null && isValid;
    }


    public boolean isAgencyValid(String agencyId) {
        Boolean isValid = agencyWebClient.get()
                .uri("/agency/validate/{agencyId}", agencyId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return isValid != null && isValid;
    }

    public boolean isVehicleValid(String vehicleId) {
        Boolean isValid = agencyWebClient.get()
                .uri("/agency/vehicle/validate/{vehicleId}", vehicleId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return isValid != null && isValid;
    }

    public boolean isVehicleAvailable(String vehicleId, LocalDate fromDate, LocalDate toDate) {

        for(LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if(redisTemplate.hasKey("vehicle:hold:"+vehicleId + ":" + date)) {
                return false;
            }
        }
        
        return true;
    }
}
