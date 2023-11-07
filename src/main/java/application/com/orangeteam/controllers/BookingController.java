package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.BookingDTO;
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

    @GetMapping(params = "customerID")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomer(@RequestParam Long customerID) {
        List<BookingDTO> bookingDTOLists = bookingService.getBookingsByCustomer(customerID);
        return ResponseEntity.ok(bookingDTOLists);
    }

    @GetMapping(params = "travelPackageID")
    public ResponseEntity<List<BookingDTO>> getBookingByTravelPackage(@RequestParam Long travelPackageID) {
        List<BookingDTO> bookingDTOLists = bookingService.getBookingsByTravelPackage(travelPackageID);
        return ResponseEntity.ok(bookingDTOLists);
    }

    @GetMapping(params = "destination")
    public ResponseEntity<List<BookingDTO>> getBookingsByDestination(@RequestParam String destination) {
        List<BookingDTO> bookingDTOList = bookingService.getBookingsByDestination(destination);
        return ResponseEntity.ok(bookingDTOList);
    }
}