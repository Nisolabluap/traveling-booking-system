package application.com.orangeteam.models.dtos;

import application.com.orangeteam.models.entities.BookingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {

    private long id;

    @NotNull
    private long customerID;

    @NotNull
    private long travelPackageID;

    @NotNull
    @Min(value = 1, message = "Cannot create booking for less than one traveler.")
    private int numTravelers;

    private double priceTotal;

    private BookingStatus bookingStatus;
}