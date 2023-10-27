package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.TravelPackageDTO;

import java.util.List;

public interface TravelPackageService {
    List<TravelPackageDTO> getAllTravelPackages();

    TravelPackageDTO createTravelPackage(TravelPackageDTO packageDTO);

    TravelPackageDTO updateTravelPackage(Long id, TravelPackageDTO packageDTO);

    void deleteTravelPackage(Long id);

    TravelPackageDTO getTravelPackageById(Long id);
}