package application.com.orangeteam.models.dtos;

import lombok.Data;

@Data
public class BookingDTO {
    private long id;
    private long customerID;
    private long travelPackageID;
    private int numTravelers;

}