package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;

public interface PaymentService {
    boolean processPayment(double paymentAmount, Long bookingId);
}