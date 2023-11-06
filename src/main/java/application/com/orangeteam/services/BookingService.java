package application.com.orangeteam.services;


import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import java.util.List;


public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO);

    List<Booking> getBookingsByCustomer(Long customerId);

    List<Booking> getBookingsByTravelPackage(Long travelPackageId);
    List<Booking> getBookingsByDestination(Long destinationId);
}