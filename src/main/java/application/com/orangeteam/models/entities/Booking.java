package application.com.orangeteam.models.entities;

import application.com.orangeteam.models.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "booking")
    List<Payment> payments = new ArrayList<>();

    @Column(name = "num_travelers")
    int numTravelers;

    @Column(name = "price_total")
    private double priceTotal;

    @Column
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;
}