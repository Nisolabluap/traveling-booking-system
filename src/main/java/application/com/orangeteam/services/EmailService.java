package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendWelcomeEmail(CustomerDTO customerDTO);
    void sendBookingConfirmationEmail(CustomerDTO customerDTO, BookingDTO bookingDTO);
    void sendPaymentReceiptEmail(CustomerDTO customerDTO);
    void sendPaymentFailedEmail(CustomerDTO customerDTO);
    void sendItineraryChangeEmail(CustomerDTO customerDTO, BookingDTO bookingDTO);
}
