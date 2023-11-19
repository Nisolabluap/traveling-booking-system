package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/bookings")
@Tag(name = "Booking API", description = "Endpoints for managing bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get booking by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Booking not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<BookingDTO> getBookingByID(@Parameter(description = "ID of the booking") @PathVariable Long id) {
        BookingDTO bookingDTO = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDTO);
    }

    @GetMapping(params = "customerID")
    @Operation(
            summary = "Get bookings by customer ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomer(@Parameter(description = "Customer ID for filtering") @RequestParam Long customerID) {
        List<BookingDTO> bookingDTOLists = bookingService.getBookingsByCustomer(customerID);
        return ResponseEntity.ok(bookingDTOLists);
    }

    @GetMapping("/by-travelPackage")
    @Operation(
            summary = "Get bookings by travel package ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<List<BookingDTO>> getBookingByTravelPackage(@Parameter(description = "Travel package ID for filtering") @RequestParam Long travelPackageID) {
        List<BookingDTO> bookingDTOLists = bookingService.getBookingsByTravelPackage(travelPackageID);
        return ResponseEntity.ok(bookingDTOLists);
    }

    @GetMapping("/by-destination")
    @Operation(
            summary = "Get bookings by destination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<List<BookingDTO>> getBookingsByDestination(@Parameter(description = "Destination for filtering") @RequestParam String destination) {
        List<BookingDTO> bookingDTOList = bookingService.getBookingsByDestination(destination);
        return ResponseEntity.ok(bookingDTOList);
    }

    @PostMapping
    @Operation(
            summary = "Create a new booking",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @PostMapping("/update-number-travelers/{id}")
    @Operation(
            summary = "Update the number of travelers for a booking",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Number of travelers updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Booking not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<BookingDTO> updateNumTravelers(@Parameter(description = "ID of the booking") @PathVariable Long id,
                                                         @RequestBody @Valid Integer numTravelers) {
        BookingDTO bookingResponseDTO = bookingService.updateNumTravelers(id, numTravelers);
        return ResponseEntity.ok(bookingResponseDTO);
    }

    @PostMapping("/cancel-booking/{id}")
    @Operation(
            summary = "Cancel a booking",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking canceled successfully"),
                    @ApiResponse(responseCode = "404", description = "Booking not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<BookingDTO> cancelBooking(@Parameter(description = "ID of the booking") @PathVariable Long id) {
        BookingDTO bookingResponseDTO = bookingService.cancel(id);
        return ResponseEntity.ok(bookingResponseDTO);
    }
}
