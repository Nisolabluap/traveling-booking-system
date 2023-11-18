package application.com.orangeteam.integration_tests;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.entities.BookingStatus;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.PaymentStatus;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
class BookingControllerTest {

    @Autowired
    TravelPackageRepository travelPackageRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackage.setDestination("London");
        travelPackage.setDescription("City by the Thames");
        travelPackage.setBookings(new ArrayList<>());
        travelPackage.setDiscountPercent(10);
        travelPackage.setPricePerPersonBeforeDiscount(1000);
        travelPackage.setAvailableReservations(100);
        travelPackage.setStartingDate(LocalDate.of(2024, 1, 1));
        travelPackage.setEndingDate(LocalDate.of(2024, 2, 1));
        travelPackageRepository.save(travelPackage);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Roland");
        customer.setLastName("Garros");
        customer.setEmail("roland@garros.tennis");
        customer.setPhoneNumber("0722987654");
        customer.setBookings(new ArrayList<>());
        customerRepository.save(customer);
    }

    @Test
    void createBookingShouldPass() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        MvcResult result = mockMvc.perform(post("/api/bookings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk()).andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        BookingDTO bookingResponseDTO = objectMapper.readValue(resultAsString, new TypeReference<>() {
        });
        assertEquals(BookingStatus.BOOKED, bookingResponseDTO.getBookingStatus());
        assertTrue(bookingResponseDTO.getId() > 0);
        assertEquals(1800, bookingResponseDTO.getPriceTotal());
    }

    @Test
    void createBookingWithInvalidCustomerIdShouldFail() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(2L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        mockMvc.perform(post("/api/bookings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookingByIdShouldPass() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        mockMvc.perform(post("/api/bookings")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)));

        MvcResult result = mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk()).andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        BookingDTO bookingResponseDTO = objectMapper.readValue(resultAsString, new TypeReference<>() {
        });

        assertEquals(BookingStatus.BOOKED, bookingResponseDTO.getBookingStatus());
        assertTrue(bookingResponseDTO.getId() > 0);
        assertEquals(1800, bookingResponseDTO.getPriceTotal());
    }

    @Test
    void getBookingWithInvalidIdShouldFail() throws Exception {
        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNumTravelersShouldPass() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        mockMvc.perform(post("/api/bookings")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)));

        Integer numTravelers = 5;
        MvcResult result = mockMvc.perform(put("/api/bookings/update-number-travelers/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(numTravelers)))
                .andExpect(status().isOk())
                .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        BookingDTO bookingResponseDTO = objectMapper.readValue(resultAsString, new TypeReference<>() {
        });

        assertEquals(5, bookingResponseDTO.getNumTravelers());
    }

    @Test
    void updateNumTravelersWithValueOverPackageLimitShouldFail() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        mockMvc.perform(post("/api/bookings")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)));

        Integer numTravelers = 200;
        mockMvc.perform(put("/api/bookings/update-number-travelers/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(numTravelers)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNumTravelersForPaidBookingShouldFail() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        mockMvc.perform(post("/api/bookings")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setBookingID(1L);
        paymentDTO.setBankAccountInfo("My personal credit card");

        boolean paymentSuccess = false;
        while (!paymentSuccess) {
            MvcResult result = mockMvc.perform(post("/api/payments")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentDTO)))
                    .andReturn();

            String resultAsString = result.getResponse().getContentAsString();
            PaymentDTO paymentResponseDTO = objectMapper.readValue(resultAsString, new TypeReference<>() {
            });
            paymentSuccess = paymentResponseDTO.getPaymentStatus() == PaymentStatus.SUCCESSFUL;
        }

        Integer numTravelers = 20;
        mockMvc.perform(put("/api/bookings/update-number-travelers/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(numTravelers)))
                .andExpect(status().isConflict());
    }

    @Test
    void cancelBookingShouldPass() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerID(1L);
        bookingDTO.setTravelPackageID(1L);
        bookingDTO.setNumTravelers(2);

        mockMvc.perform(post("/api/bookings")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)));

        MvcResult result = mockMvc.perform(put("/api/bookings/cancel-booking/1"))
                .andExpect(status().isOk())
                .andReturn();
        String resultAsString = result.getResponse().getContentAsString();
        BookingDTO bookingResponseDTO = objectMapper.readValue(resultAsString, new TypeReference<>() {
        });

        assertSame(BookingStatus.CANCELLED, bookingResponseDTO.getBookingStatus());
    }
}
