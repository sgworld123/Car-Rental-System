package com.CarRentalSystem.AgencyService.Repository;

import com.CarRentalSystem.AgencyService.Dto.AgencySearchResponse;
import com.CarRentalSystem.AgencyService.Model.Agency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgencyRepository extends MongoRepository<Agency,String> {
    List<Agency> findBySourceCity(String sourceCity);

}
