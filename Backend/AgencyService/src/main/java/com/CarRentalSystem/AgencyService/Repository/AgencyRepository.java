package com.CarRentalSystem.AgencyService.Repository;

import com.CarRentalSystem.AgencyService.Model.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AgencyRepository extends MongoRepository<Agency,String> {

    Page<Agency> findBySourceCity(String sourceCity, Pageable pageable);
}
