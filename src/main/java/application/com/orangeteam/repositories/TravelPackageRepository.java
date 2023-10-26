package application.com.orangeteam.repositories;

import application.com.orangeteam.models.entities.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {

    @Query("SELECT tp FROM TravelPackage tp " +
            "WHERE tp.name = :name " +
            "AND tp.destination = :destination " +
            "AND tp.description = :description " +
            "AND tp.pricePerPerson = :pricePerPerson " +
            "AND tp.startingDate = :startingDate " +
            "AND tp.endingDate = :endingDate")
    TravelPackage findByAllFields(
            @Param("name") String name,
            @Param("destination") String destination,
            @Param("description") String description,
            @Param("pricePerPerson") double pricePerPerson,
            @Param("startingDate") LocalDate startingDate,
            @Param("endingDate") LocalDate endingDate
    );
}