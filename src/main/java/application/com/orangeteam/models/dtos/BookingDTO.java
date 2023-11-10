package application.com.orangeteam.models.dtos;

import application.com.orangeteam.models.entities.BookingStatus;
import lombok.Data;

@Data
public class BookingDTO {
    private long id;
    private long customerID;
    private long travelPackageID;
    private int numTravelers;
    private double priceTotal;
    private BookingStatus bookingStatus;

}