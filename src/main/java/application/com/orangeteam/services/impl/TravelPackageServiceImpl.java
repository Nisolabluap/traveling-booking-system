package application.com.orangeteam.services.impl;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.TravelPackageRepository;
import application.com.orangeteam.services.TravelPackageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TravelPackageServiceImpl implements TravelPackageService {
    @Autowired
    private TravelPackageRepository travelPackageRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<TravelPackageDTO> getAllTravelPackages() {
        List<TravelPackage> packages = travelPackageRepository.findAll();
        return packages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TravelPackageDTO createTravelPackage(TravelPackageDTO packageDTO) {
        // Check if a package with the same name, destination, starting date, and ending date already exists
        TravelPackage existingPackage = travelPackageRepository.findByAllFields(
                packageDTO.getName(),
                packageDTO.getDestination(),
                packageDTO.getStartingDate(),
                packageDTO.getEndingDate()
        );

        if (existingPackage != null) {
            // A similar package exists, check if both description and price are different
            if (!existingPackage.getDescription().equals(packageDTO.getDescription()) &&
                    existingPackage.getPricePerPerson() != packageDTO.getPricePerPerson()) {
                // Both description and price are different, create a new package
                return createNewTravelPackage(packageDTO);
            } else {
                // Either description or price (or both) are the same, return the existing package
                return convertToDTO(existingPackage);
            }
        } else {
            // No similar package exists, create a new package
            return createNewTravelPackage(packageDTO);
        }
    }

    private TravelPackageDTO createNewTravelPackage(TravelPackageDTO packageDTO) {
        LocalDate startDate = packageDTO.getStartingDate();
        LocalDate endDate = packageDTO.getEndingDate();

        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("End date must be after the start date.");
        }

        int duration = Period.between(startDate, endDate).getDays();

        if (duration < 2) {
            throw new IllegalArgumentException("Duration must be greater than 1.");
        }

        packageDTO.setDuration(duration);

        TravelPackage savedPackage = travelPackageRepository.save(convertToEntity(packageDTO));
        return convertToDTO(savedPackage);
    }

    @Override
    public TravelPackageDTO updateTravelPackage(Long id, TravelPackageDTO packageDTO) {
        Optional<TravelPackage> existingPackageOptional = travelPackageRepository.findById(id);

        if (existingPackageOptional.isPresent()) {
            TravelPackage existingPackage = existingPackageOptional.get();
            LocalDate startDate = packageDTO.getStartingDate();
            LocalDate endDate = packageDTO.getEndingDate();
            int duration = Period.between(startDate, endDate).getDays();

            existingPackage.setName(packageDTO.getName());
            existingPackage.setDestination(packageDTO.getDestination());
            existingPackage.setDescription(packageDTO.getDescription());
            existingPackage.setPricePerPerson(packageDTO.getPricePerPerson());
            existingPackage.setStartingDate(packageDTO.getStartingDate());
            existingPackage.setEndingDate(packageDTO.getEndingDate());

            TravelPackage updatedPackage = travelPackageRepository.save(existingPackage);
            return convertToDTO(updatedPackage);
        } else {
            throw new EntityNotFoundException("Travel package with ID " + id + " not found");
        }
    }

    @Override
    public void deleteTravelPackage(Long id) {
        travelPackageRepository.deleteById(id);
    }

    @Override
    public TravelPackageDTO getTravelPackageById(Long id) {
        Optional<TravelPackage> packageOptional = travelPackageRepository.findById(id);
        if (packageOptional.isPresent()) {
            TravelPackage travelPackage = packageOptional.get();
            return convertToDTO(travelPackage);
        } else {
            throw new EntityNotFoundException("Travel package with ID " + id + " not found");
        }
    }

    private TravelPackageDTO convertToDTO(TravelPackage travelPackage) {
        TravelPackageDTO packageDTO = new TravelPackageDTO();
        BeanUtils.copyProperties(travelPackage, packageDTO);
        return packageDTO;
    }

    private TravelPackage convertToEntity(TravelPackageDTO packageDTO) {
        TravelPackage travelPackage = new TravelPackage();
        BeanUtils.copyProperties(packageDTO, travelPackage);
        return travelPackage;
    }

    @Override
    public List<TravelPackageDTO> getTravelPackageBetweenDates(LocalDate startingDate, LocalDate endingDate, boolean ascending) {
        List<TravelPackage> allPackages = travelPackageRepository.findAll();

        List<TravelPackage> filteredPackages = allPackages.stream()
                .filter(travelPackage -> travelPackage.getStartingDate().isAfter(startingDate) && travelPackage.getEndingDate().isBefore(endingDate))
                .collect(Collectors.toList());

        //sorting dates
        if (ascending) {
            filteredPackages.sort(Comparator.comparing(TravelPackage::getStartingDate));
        } else {
            filteredPackages.sort(Comparator.comparing(TravelPackage::getStartingDate).reversed());
        }

        List<TravelPackageDTO> filteredPackagesDTO = new ArrayList<>();
        for (TravelPackage travelPackage : filteredPackages) {
            filteredPackagesDTO.add(objectMapper.convertValue(travelPackage, TravelPackageDTO.class));

        }
        return filteredPackagesDTO;
    }

    @Override
    public List<TravelPackageDTO> getTravelPackageWithPriceBetweenTwoValues(double minPrice, double maxPrice) {
        List<TravelPackage> allPackages = travelPackageRepository.findAll();
        List<TravelPackageDTO> travelPackagesPriceDTOs = new ArrayList<>();
        for (TravelPackage travelPackage : allPackages) {
            if(travelPackage.getPricePerPerson()> minPrice && travelPackage.getPricePerPerson() <= maxPrice){
                travelPackagesPriceDTOs.add(objectMapper.convertValue(travelPackage, TravelPackageDTO.class));
            }
        }
        return travelPackagesPriceDTOs;
    }

    @Override
    public List<TravelPackageDTO> getTravelPackageByDestination(String destination) {
        List<TravelPackage> travelPackages = travelPackageRepository.findByDestination(destination);

        if (travelPackages.isEmpty()) {
            throw new EntityNotFoundException("No travel packages found for destination: " + destination);
        }

        List<TravelPackageDTO> travelPackageDTOs = new ArrayList<>();
        for (TravelPackage travelPackage : travelPackages) {
            travelPackageDTOs.add(convertToDTO(travelPackage));
        }

        return travelPackageDTOs;
    }
}