package com.orangeteam.services;

import com.orangeteam.models.dtos.CustomerDTO;
import com.orangeteam.models.dtos.TravelPackageDTO;
import com.orangeteam.models.entities.Booking;
import com.orangeteam.models.entities.Customer;

public class BookingServiceImpl implements BookingService {
    @Override
    public void createBooking(CustomerDTO customerDTO, TravelPackageDTO travelPackageDTO, int numTravelers) {
        Booking booking = new Booking();
    }
}
