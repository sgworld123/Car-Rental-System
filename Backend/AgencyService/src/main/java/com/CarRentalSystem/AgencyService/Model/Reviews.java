package com.CarRentalSystem.AgencyService.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reviews {
    private String name;
    private String date;
    private String text;
    private String avatar;
}
