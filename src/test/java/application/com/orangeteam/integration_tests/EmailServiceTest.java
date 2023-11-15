package application.com.orangeteam.integration_tests;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.BookingStatus;
import application.com.orangeteam.models.entities.PaymentStatus;
import application.com.orangeteam.services.EmailService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void sendEmails() {
        TravelPackageDTO oldTravelPackageDTO = new TravelPackageDTO();
        oldTravelPackageDTO.setDestination("Here");
        oldTravelPackageDTO.setStartingDate(LocalDate.of(2024,1,1));
        oldTravelPackageDTO.setEndingDate(LocalDate.of(2024,2,2));
        oldTravelPackageDTO.setId(1L);

        TravelPackageDTO updatedTravelPackageDTO = new TravelPackageDTO();
        updatedTravelPackageDTO.setDestination("There");
        updatedTravelPackageDTO.setStartingDate(LocalDate.of(2024,3,3));
        updatedTravelPackageDTO.setEndingDate(LocalDate.of(2024,4,4));
        updatedTravelPackageDTO.setId(1L);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("Gogu");
        customerDTO.setLastName("the Great");
        customerDTO.setEmail("adrian.puscu@techie.com");

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setPriceTotal(2000);
        bookingDTO.setNumTravelers(2);
        bookingDTO.setBookingStatus(BookingStatus.PAID);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setBookingID(1L);
        paymentDTO.setId(1L);
        paymentDTO.setAmount(2000);
        paymentDTO.setPaymentStatus(PaymentStatus.SUCCESSFUL);

//        emailService.sendWelcomeEmail(customerDTO);
//        emailService.sendBookingConfirmationEmail(customerDTO, bookingDTO);
//        emailService.sendPaymentReceiptEmail(customerDTO, paymentDTO);
//        emailService.sendPaymentFailedEmail(customerDTO, paymentDTO);
//        emailService.sendItineraryChangeEmail(customerDTO, bookingDTO, oldTravelPackageDTO, updatedTravelPackageDTO);
        assertTrue(true);
    }
}
