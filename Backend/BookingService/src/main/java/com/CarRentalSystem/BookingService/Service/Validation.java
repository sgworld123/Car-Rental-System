package com.CarRentalSystem.BookingService.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserValidation {
    private final WebClient userWebClient;

    public boolean isUserValid(String userId) {
        Boolean isValid = userWebClient.get()
                .uri("/user/validate/{userId}", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        return isValid != null && isValid;
    }

}
