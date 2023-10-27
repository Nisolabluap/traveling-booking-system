package application.com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    private double pricePerPerson;

    @Column(name = "starting_date")
    private LocalDate startingDate;
    @Column(name = "ending_date")
    private LocalDate endingDate;
}