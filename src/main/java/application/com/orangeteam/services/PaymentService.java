package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.PaymentDTO;

public interface PaymentService {
    PaymentDTO processPayment(String creditCardNumber, Long bookingId);

    void reimburse(Long paymentID);
}