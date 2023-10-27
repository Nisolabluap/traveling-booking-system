package com.orangeteam.services;

import com.orangeteam.models.dtos.BookingDTO;
import com.orangeteam.models.dtos.CustomerDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendWelcomeEmail(CustomerDTO customerDTO);
    void sendBookingConfirmationEmail(CustomerDTO customerDTO, BookingDTO bookingDTO);
    void sendPaymentReceiptEmail(CustomerDTO customerDTO);
    void sendPaymentFailedEmail(CustomerDTO customerDTO);
    void sendItineraryChangeEmail(CustomerDTO customerDTO, BookingDTO bookingDTO);
    void sendEmail(String subject, String body, String eMailAddress);
}
