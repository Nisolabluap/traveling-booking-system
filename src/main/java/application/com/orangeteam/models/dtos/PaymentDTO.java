package application.com.orangeteam.models.dtos;

import application.com.orangeteam.models.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private Long id;

    private LocalDateTime paymentDateTime;

    private double amount;

    @NotEmpty
    private Long bookingID;

    @NotEmpty
    private String bankAccountInfo;

    private PaymentStatus paymentStatus;
}