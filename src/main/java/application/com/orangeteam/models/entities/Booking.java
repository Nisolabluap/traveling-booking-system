package application.com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "travel_package_id", nullable = false)
    TravelPackage travelPackage;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    List<Payment> payments = new ArrayList<>();

    @Column(name = "num_travelers")
    int numTravelers;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    public double getTotalAmount(){
        if(travelPackage != null){
            return numTravelers * travelPackage.getPricePerPerson();
        } else {
            return 0.0;
        }
    }

    public double getDiscount(){
        if (!payments.isEmpty()) {
            Payment firstPayment = payments.get(0);
            return firstPayment.getDiscount();
        } else {
            return 0.0;
        }
    }
}