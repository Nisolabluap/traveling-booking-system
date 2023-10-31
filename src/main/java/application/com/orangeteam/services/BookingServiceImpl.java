package application.com.orangeteam.services;


import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.Booking;

public class BookingServiceImpl implements BookingService {
    @Override
    public void createBooking(CustomerDTO customerDTO, TravelPackageDTO travelPackageDTO, int numTravelers) {
        Booking booking = new Booking();
    }
}
