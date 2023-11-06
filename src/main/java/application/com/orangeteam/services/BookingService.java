package application.com.orangeteam.services;


import application.com.orangeteam.models.dtos.BookingDTO;


public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO);
}