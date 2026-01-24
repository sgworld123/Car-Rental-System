package com.CarRentalSystem.AgencyService.Repository;

import com.CarRentalSystem.AgencyService.Model.Agency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends MongoRepository<Agency,String> {
}
