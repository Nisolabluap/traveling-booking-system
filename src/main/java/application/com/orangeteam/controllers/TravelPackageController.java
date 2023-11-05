package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.services.TravelPackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Travel Package API", description = "Operations for managing travel packages")
@RestController
@RequestMapping("/api/travel-packages")
public class TravelPackageController {
    @Autowired
    private TravelPackageService travelPackageService;

    @ApiOperation(value = "Get all travel packages")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Travel packages retrieved successfully"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping
    public List<TravelPackageDTO> getAllTravelPackages() {
        return travelPackageService.getAllTravelPackages();
    }

    @ApiOperation(value = "Create a new travel package")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Travel package created successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping
    public TravelPackageDTO createTravelPackage(@RequestBody TravelPackageDTO packageDTO) {
        return travelPackageService.createTravelPackage(packageDTO);
    }

    @ApiOperation(value = "Update a travel package by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Travel package updated successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Travel package not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public TravelPackageDTO updateTravelPackage(@PathVariable Long id, @RequestBody TravelPackageDTO packageDTO) {
        return travelPackageService.updateTravelPackage(id, packageDTO);
    }

    @ApiOperation(value = "Delete a travel package by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Travel package deleted successfully"),
            @ApiResponse(code = 404, message = "Travel package not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelPackage(@PathVariable Long id) {
        travelPackageService.deleteTravelPackage(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get a travel package by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Travel package retrieved successfully"),
            @ApiResponse(code = 404, message = "Travel package not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public TravelPackageDTO getTravelPackage(@PathVariable Long id) {
        return travelPackageService.getTravelPackageById(id);
    }
}