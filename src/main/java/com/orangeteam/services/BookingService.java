package com.orangeteam.services;

import com.orangeteam.models.dtos.CustomerDTO;
import com.orangeteam.models.dtos.TravelPackageDTO;
import com.orangeteam.models.entities.Customer;

public interface BookingService {
     void createBooking(CustomerDTO customerDTO, TravelPackageDTO travelPackageDTO, int numTravelers);
}
