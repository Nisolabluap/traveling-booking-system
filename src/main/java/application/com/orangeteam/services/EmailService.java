package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.dtos.TravelPackageDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendWelcomeEmail(CustomerDTO customerDTO);
    void sendBookingConfirmationEmail(CustomerDTO customerDTO, BookingDTO bookingDTO);
    void sendPaymentReceiptEmail(CustomerDTO customerDTO, PaymentDTO paymentDTO);
    void sendPaymentFailedEmail(CustomerDTO customerDTO, PaymentDTO paymentDTO);
    void sendItineraryChangeEmail(CustomerDTO customerDTO, BookingDTO bookingDTO, TravelPackageDTO oldTravelPackageDTO, TravelPackageDTO updatedTravelPackage);
}
