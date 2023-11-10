package application.com.orangeteam.services;


import application.com.orangeteam.models.dtos.BookingDTO;

import java.util.List;


public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO);

    List<BookingDTO> getBookingsByCustomer(Long customerId);

    List<BookingDTO> getBookingsByTravelPackage(Long travelPackageId);

    List<BookingDTO> getBookingsByDestination(String destination);
    BookingDTO getBookingById(Long id);

    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);

    BookingDTO cancel(Long id);
}