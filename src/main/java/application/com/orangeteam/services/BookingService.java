package application.com.orangeteam.services;


import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.TravelPackageDTO;

public interface BookingService {
     void createBooking(CustomerDTO customerDTO, TravelPackageDTO travelPackageDTO, int numTravelers);
}
