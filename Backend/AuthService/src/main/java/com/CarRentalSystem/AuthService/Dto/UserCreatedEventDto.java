package com.CarRentalSystem.AuthService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreatedEventDto {
    private String userName;
    private String userEmail;
    private String phoneNumber;

}
