package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.services.TravelPackageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/travel-packages")
public class TravelPackageController {
    @Autowired
    private TravelPackageService travelPackageService;

    @GetMapping
    public List<TravelPackageDTO> getAllTravelPackages() {
        return travelPackageService.getAllTravelPackages();
    }

    @PostMapping
    public TravelPackageDTO createTravelPackage(@RequestBody TravelPackageDTO packageDTO) {
        return travelPackageService.createTravelPackage(packageDTO);
    }

    @PutMapping("/{id}")
    public TravelPackageDTO updateTravelPackage(@PathVariable Long id, @RequestBody TravelPackageDTO packageDTO) {
        return travelPackageService.updateTravelPackage(id, packageDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelPackage(@PathVariable Long id) {
        travelPackageService.deleteTravelPackage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public TravelPackageDTO getTravelPackage(@PathVariable Long id) {
        return travelPackageService.getTravelPackageById(id);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<TravelPackageDTO>> getTravelPackagesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate,
            @RequestParam(defaultValue = "true") boolean ascending) {

        return ResponseEntity.ok(travelPackageService.getTravelPackageBetweenDates(startingDate, endingDate, ascending));
    }

    @GetMapping("/by-price-range")
    public ResponseEntity<List<TravelPackageDTO>> getTravelPackageWithPriceBetweenTwoValues(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return ResponseEntity.ok(travelPackageService.getTravelPackageWithPriceBetweenTwoValues(minPrice, maxPrice));
    }

    @GetMapping("/by-destination")
    public ResponseEntity<List<TravelPackageDTO>> getTravelPackagesByDestination(@RequestParam String destination) {
        try {
            List<TravelPackageDTO> travelPackages = travelPackageService.getTravelPackageByDestination(destination);
            return ResponseEntity.ok(travelPackages);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}