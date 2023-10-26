package application.com.orangeteam.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
public class TravelPackageDTO {
    private Long id;

    @NotEmpty(message = "This field must not be empty!")
    private String name;

    @NotEmpty(message = "This field must not be empty!")
    private String destination;

    @NotEmpty(message = "This field must not be empty!")
    private String description;

    private int duration;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double pricePerPerson;

    @NotEmpty(message = "This field must not be empty!")
    private LocalDate startingDate;

    @NotEmpty(message = "This field must not be empty!")
    private LocalDate endingDate;

    // Calculate the duration in days based on starting and ending dates
    public int getDuration() {
        Period period = Period.between(startingDate, endingDate);
        int days = period.getDays();
        int months = period.getMonths();
        int years = period.getYears();

        // Convert years and months into days (considering an average of 30.44 days per month)
        int totalDays = years * 365 + months * 30 + days;

        return totalDays;
    }
}