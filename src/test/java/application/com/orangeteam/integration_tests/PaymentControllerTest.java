package application.com.orangeteam.integration_tests;

import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.entities.*;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TravelPackageRepository travelPackageRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void makePaymentShouldPass() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customerRepository.save(customer);

        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackageRepository.save(travelPackage);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTravelPackage(travelPackage);
        booking.setCustomer(customer);
        booking.setNumTravelers(20);
        booking.setPriceTotal(2000);
        booking.setBookingStatus(BookingStatus.BOOKED);
        bookingRepository.save(booking);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setBookingID(1L);
        paymentDTO.setBankAccountInfo("My personal credit card");

        MvcResult result = mockMvc.perform(post("/api/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        PaymentDTO paymentDTOConverted = objectMapper.readValue(resultAsString, new TypeReference<>() {
        });

        assertTrue(paymentDTOConverted.getId() > 0);
        assertTrue(paymentDTOConverted.getPaymentDateTime().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals(paymentDTOConverted.getBankAccountInfo(), paymentDTO.getBankAccountInfo());
        assertEquals(paymentDTOConverted.getAmount(), booking.getPriceTotal());
        assertEquals(paymentDTOConverted.getBookingID(), paymentDTO.getBookingID());
        assertTrue(paymentDTOConverted.getPaymentStatus() == PaymentStatus.SUCCESSFUL ||
                paymentDTOConverted.getPaymentStatus() == PaymentStatus.FAILED);
    }

    @Test
    void makePaymentWithNoBookingShouldFail() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setBookingID(1L);
        paymentDTO.setBankAccountInfo("My personal credit card");

        mockMvc.perform(post("/api/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void makePaymentForPaidOrCanceledBookingShouldFail() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customerRepository.save(customer);

        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackageRepository.save(travelPackage);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTravelPackage(travelPackage);
        booking.setCustomer(customer);
        booking.setNumTravelers(20);
        booking.setPriceTotal(2000);
        booking.setBookingStatus(BookingStatus.PAID);
        bookingRepository.save(booking);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setBookingID(1L);
        paymentDTO.setBankAccountInfo("My personal credit card");

        mockMvc.perform(post("/api/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isConflict());
    }
}
