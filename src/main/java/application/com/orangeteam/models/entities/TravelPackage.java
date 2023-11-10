package application.com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "packages")
public class TravelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "destination")
    private String destination;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double pricePerPersonBeforeDiscount;

    @Column(name = "discount")
    private int discountPercent;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "ending_date")
    private LocalDate endingDate;

    @Column(name = "available_reservations")
    private int availableReservations;

    @OneToMany(mappedBy = "travelPackage")
    private List<Booking> bookings;
}