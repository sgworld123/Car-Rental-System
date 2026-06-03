package com.CarRentalSystem.UserService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEventDto {
    private String userName;
    private String userEmail;
    private String phoneNumber;
    private String imageUrl;

}
