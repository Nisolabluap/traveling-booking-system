package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.services.TravelPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/travel-packages")
@Tag(name = "Travel package API", description = "Endpoints for managing travel packages")
public class TravelPackageController {

    @Autowired
    private TravelPackageService travelPackageService;

    @GetMapping
    @Operation(
            summary = "Get all travel packages",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel packages retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public List<TravelPackageDTO> getAllTravelPackages() {
        return travelPackageService.getAllTravelPackages();
    }

    @PostMapping
    @Operation(
            summary = "Create a new travel package",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel package created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public TravelPackageDTO createTravelPackage(@RequestBody @Valid TravelPackageDTO packageDTO) {
        return travelPackageService.createTravelPackage(packageDTO);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a travel package by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel package updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Travel package not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public TravelPackageDTO updateTravelPackage(
            @Parameter(description = "ID of the travel package") @PathVariable Long id,
            @RequestBody @Valid TravelPackageDTO packageDTO) {
        return travelPackageService.updateTravelPackage(id, packageDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a travel package by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Travel package deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Travel package not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<Void> deleteTravelPackage(
            @Parameter(description = "ID of the travel package") @PathVariable Long id) {
        travelPackageService.deleteTravelPackage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a travel package by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel package retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Travel package not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public TravelPackageDTO getTravelPackage(
            @Parameter(description = "ID of the travel package") @PathVariable Long id) {
        return travelPackageService.getTravelPackageById(id);
    }

    @GetMapping("/between-dates")
    @Operation(
            summary = "Get travel packages between specified dates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel packages retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<List<TravelPackageDTO>> getTravelPackagesBetweenDates(
            @Parameter(description = "Start date for filtering") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
            @Parameter(description = "End date for filtering") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate,
            @Parameter(description = "Ascending order flag") @RequestParam(defaultValue = "true") boolean ascending) {
        return ResponseEntity.ok(travelPackageService.getTravelPackageBetweenDates(startingDate, endingDate, ascending));
    }

    @GetMapping("/by-price-range")
    @Operation(
            summary = "Get travel packages within a specified price range",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel packages retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<List<TravelPackageDTO>> getTravelPackageWithPriceBetweenTwoValues(
            @Parameter(description = "Minimum price for filtering") @RequestParam double minPrice,
            @Parameter(description = "Maximum price for filtering") @RequestParam double maxPrice) {
        return ResponseEntity.ok(travelPackageService.getTravelPackageWithPriceBetweenTwoValues(minPrice, maxPrice));
    }

    @GetMapping("/by-destination")
    @Operation(
            summary = "Get travel packages by destination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Travel packages retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Travel package not found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<List<TravelPackageDTO>> getTravelPackagesByDestination(
            @Parameter(description = "Destination for filtering") @RequestParam @Valid String destination) {
        try {
            List<TravelPackageDTO> travelPackages = travelPackageService.getTravelPackageByDestination(destination);
            return ResponseEntity.ok(travelPackages);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
