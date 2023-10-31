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
    @PrimaryKeyJoinColumn(name = "customer")
    @OneToOne()
    Customer customer;
    @PrimaryKeyJoinColumn(name = "travel_package")
    @OneToOne()
    TravelPackage travelPackage;
    @PrimaryKeyJoinColumn(name = "payment")
    @OneToOne()
    Payment payment;
    @Column(name = "num_travelers")
    int numTravelers;
}