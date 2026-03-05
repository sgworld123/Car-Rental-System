package com.CarRentalSystem.PaymentService.Repository;

import com.CarRentalSystem.PaymentService.Models.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
