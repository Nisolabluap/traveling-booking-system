package application.com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "discount")
    private double discount;
    @Column(name = "total_payment")
    private double totalPayment;

}