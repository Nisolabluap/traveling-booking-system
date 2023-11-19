package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.TravelPackageDTO;

import java.time.LocalDate;
import java.util.List;

public interface TravelPackageService {
    List<TravelPackageDTO> getAllTravelPackages();
    TravelPackageDTO createTravelPackage(TravelPackageDTO packageDTO);
    TravelPackageDTO updateTravelPackage(Long id, TravelPackageDTO packageDTO);
    void deleteTravelPackage(Long id);
    TravelPackageDTO getTravelPackageById(Long id);
    List<TravelPackageDTO> getTravelPackageBetweenDates(LocalDate startingDate, LocalDate endingDate, boolean ascending);
    List<TravelPackageDTO> getTravelPackageWithPriceBetweenTwoValues(double minPrice, double maxPrice, boolean ascending);
    List<TravelPackageDTO> getTravelPackageByDestination(String destination);
}