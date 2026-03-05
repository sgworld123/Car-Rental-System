package com.CarRentalSystem.AgencyService.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "vehicleData")
public class Vehicle {
    private String vehicleId;
    private String carNumber;
    private String driverName;
    private ModelType carModel;
    private String description;
    private float pricePerKm;
    private List<Reviews> reviews;
    private String coverImage;
    private List<String> images;
}
