package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.services.TravelPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}