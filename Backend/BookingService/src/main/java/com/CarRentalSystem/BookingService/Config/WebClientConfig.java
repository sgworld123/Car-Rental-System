package com.CarRentalSystem.BookingService.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient agencyWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://localhost:8090/api/agency")
                .build();
    }
    @Bean
    public WebClient userWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://localhost:8090/api/user")
                .build();
    }

}
