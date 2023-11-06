package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Destination;
import application.com.orangeteam.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/bookings")
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @GetMapping("/api/bookings/customer")
    public ResponseEntity<List<Booking>> getBookingsByCustomer(@Valid @RequestBody BookingDTO bookingDTO) {
        Long customerId = bookingDTO.getCustomerID();
        List<Booking> bookings = bookingService.getBookingsByCustomer(customerId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/api/bookings/customer/travelpackage")
    public ResponseEntity<List<Booking>> getBookingByTravelPackage(@Valid @RequestBody BookingDTO bookingDTO) {
        Long travelPackageId = bookingDTO.getTravelPackageID();
        List<Booking> bookings = bookingService.getBookingsByTravelPackage(travelPackageId);

        return ResponseEntity.ok(bookings);
    }

    @RequestMapping(name = "api/bookings/customer/destination")
    Long destinationId = BookingDTO();
    List<Booking> bookings = bookingService.getBookingsByDestination(destinationId);

}