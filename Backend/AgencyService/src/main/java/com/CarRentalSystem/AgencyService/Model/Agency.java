package com.CarRentalSystem.AgencyService.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "AgencyData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Agency {
    @Id
    private String id;
    private String name;
    private String email;
    private String details;
    private String rating;
    private List<Reviews> reviews;
    private String agencyImage;
    private long phone;
    private String address;
    private String sourceCity;
    private List<Vehicle> vehicleInfo;
}
