package application.com.orangeteam.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class PaymentDTO {
    private UUID id;
    private double discount;
    private String booking;
    private LocalDateTime paymentDate;
    private double totalPayment;
    private int numTravelers;
}