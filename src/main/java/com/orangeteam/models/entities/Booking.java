package com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "customer")
    @OneToOne(mappedBy = "customers")
    Customer customer;
    @Column(name = "travel_package")
    @OneToOne(mappedBy = "packages")
    TravelPackage travelPackage;
    @Column(name = "payment")
    @OneToOne(mappedBy = "payments")
    Payment payment;
    @Column(name = "num_travelers")
    int numTravelers;
}