package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }
}