package application.com.orangeteam.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = " destination")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "location")
    private String location;
    @Column(name = "distance")
    private double distance;

    @ManyToMany(mappedBy = "destination")
    private List<Destination> destinations;
}