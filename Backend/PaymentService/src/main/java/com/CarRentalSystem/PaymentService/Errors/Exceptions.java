package com.CarRentalSystem.PaymentService.Errors;

public class Exceptions {
    public static class PaymentProcessingException extends RuntimeException {
        public PaymentProcessingException(String message) {
            super(message);
        }
    }

}
