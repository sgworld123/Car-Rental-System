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
    private long phone;
    private List<String> cities;
    private String password;
    private List<Vehicle> vehicleInfo;
}
