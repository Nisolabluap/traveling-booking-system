package application.com.orangeteam.models.dtos;

import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.Payment;
import application.com.orangeteam.models.entities.TravelPackage;
import lombok.Data;

@Data
public class BookingDTO {
    private long id;
    Customer customer;
    TravelPackage travelPackage;
    Payment payment;
}