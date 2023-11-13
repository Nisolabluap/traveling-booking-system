package application.com.orangeteam.models.dtos;

import application.com.orangeteam.models.entities.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private LocalDateTime paymentDate;
    private double amount;
    private Long bookingID;
    private String bankAccountInfo;
    private PaymentStatus paymentStatus;
}