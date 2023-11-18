package application.com.orangeteam.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TravelPackageDTO {
    private Long id;

    @NotEmpty(message = "This field must not be empty!")
    private String name;

    @NotEmpty(message = "This field must not be empty!")
    private String destination;

    @NotEmpty(message = "This field must not be empty!")
    private String description;

    @NotNull(message = "This field must not be null")
    @Min(value = 0, message = "Available reservations must be greater than or equal to 0")
    private int availableReservations;

    private int duration;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double pricePerPersonBeforeDiscount;

    @Min(value = 0, message = "Discount percentage cannot be negative.")
    private int discountPercent;

    @NotNull(message = "This field must not be null!")
    private LocalDate startingDate;

    @NotNull(message = "This field must not be null!")
    private LocalDate endingDate;

}