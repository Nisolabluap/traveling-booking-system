package com.orangeteam.models.dtos;

import com.orangeteam.models.entities.Customer;
import com.orangeteam.models.entities.Payment;
import com.orangeteam.models.entities.TravelPackage;
import lombok.Data;

@Data
public class BookingDTO {
    private long id;
    Customer customer;
    TravelPackage travelPackage;
    Payment payment;
}