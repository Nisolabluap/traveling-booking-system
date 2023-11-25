package application.com.orangeteam.models.dtos;

import application.com.orangeteam.models.enums.BookingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {

    private Long id;

    @NotNull
    private Long customerID;

    @NotNull
    private Long travelPackageID;

    @NotNull
    @Min(value = 1, message = "Cannot create booking for less than one traveler.")
    private int numTravelers;

    private double totalPrice;

    private BookingStatus bookingStatus;
}