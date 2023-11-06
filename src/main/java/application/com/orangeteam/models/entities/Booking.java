package application.com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

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
    /*@PrimaryKeyJoinColumn(name = "payment")
    @OneToOne
    Payment payment;*/
    @Column(name = "num_travelers")
    int numTravelers;
}