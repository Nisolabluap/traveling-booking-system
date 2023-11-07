package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.DuplicateTravelPackageException;
import application.com.orangeteam.exceptions.TravelPackageCreateException;
import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TravelPackageServiceImpl implements TravelPackageService {

    private final TravelPackageRepository travelPackageRepository;
    private final ObjectMapper objectMapper;

    public TravelPackageServiceImpl(TravelPackageRepository travelPackageRepository, ObjectMapper objectMapper) {
        this.travelPackageRepository = travelPackageRepository;
        this.objectMapper = objectMapper;
    }

    @NotNull
    private static TravelPackage updateTravelPackageInfo(TravelPackageDTO packageDTO, TravelPackage existingPackage) {
        existingPackage.setName(packageDTO.getName());
        existingPackage.setDestination(packageDTO.getDestination());
        existingPackage.setDescription(packageDTO.getDescription());
        existingPackage.setPricePerPerson(packageDTO.getPricePerPerson());
        existingPackage.setStartingDate(packageDTO.getStartingDate());
        existingPackage.setEndingDate(packageDTO.getEndingDate());
        existingPackage.setAvailableReservations(packageDTO.getAvailableReservations());
        return existingPackage;
    }

    @Override
    public List<TravelPackageDTO> getAllTravelPackages() {
        List<TravelPackage> packages = travelPackageRepository.findAll();
        return packages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public TravelPackageDTO createTravelPackage(TravelPackageDTO packageDTO) {
        if (!isValid(packageDTO)) {
            throw new TravelPackageCreateException("Invalid dates");
        }
        if (isDuplicate(packageDTO)) {
            throw new DuplicateTravelPackageException("Travel package already exists");
        }
        TravelPackage savedPackage = travelPackageRepository.save(convertToEntity(packageDTO));

        return convertToDTO(savedPackage);
    }

    @Override
    public TravelPackageDTO updateTravelPackage(Long id, TravelPackageDTO packageDTO) {
        if (!isValid(packageDTO)) {
            throw new TravelPackageCreateException("Invalid dates");
        }

        Optional<TravelPackage> existingPackageOptional = travelPackageRepository.findById(id);
        if (existingPackageOptional.isPresent()) {
            TravelPackage existingPackage = updateTravelPackageInfo(packageDTO, existingPackageOptional.get());
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
            if (travelPackage.getPricePerPerson() > minPrice && travelPackage.getPricePerPerson() <= maxPrice) {
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

    private boolean isValid(TravelPackageDTO travelPackageDTO) {
        return travelPackageDTO.getStartingDate().isAfter(LocalDate.now()) &&
                travelPackageDTO.getStartingDate().plusDays(2).isBefore(travelPackageDTO.getEndingDate());
    }

    private boolean isDuplicate(TravelPackageDTO travelPackageDTO) {
        TravelPackage existingPackage = travelPackageRepository.findByAllFields(
                travelPackageDTO.getName(),
                travelPackageDTO.getDestination(),
                travelPackageDTO.getDescription(),
                travelPackageDTO.getStartingDate(),
                travelPackageDTO.getEndingDate()
        );

        return existingPackage != null;
    }
}