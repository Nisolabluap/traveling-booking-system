package application.com.orangeteam.services.impl;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.TravelPackageRepository;
import application.com.orangeteam.services.TravelPackageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TravelPackageServiceImpl implements TravelPackageService {
    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @Override
    public List<TravelPackageDTO> getAllTravelPackages() {
        List<TravelPackage> packages = travelPackageRepository.findAll();
        return packages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TravelPackageDTO createTravelPackage(TravelPackageDTO packageDTO) {
        TravelPackage existingPackage = travelPackageRepository.findByAllFields(
                packageDTO.getName(),
                packageDTO.getDestination(),
                packageDTO.getDescription(),
                packageDTO.getPricePerPerson(),
                packageDTO.getStartingDate(),
                packageDTO.getEndingDate()
        );

        if (existingPackage != null) {
            return convertToDTO(existingPackage);
        } else {
            LocalDate startDate = packageDTO.getStartingDate();
            LocalDate endDate = packageDTO.getEndingDate();

            if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
                throw new IllegalArgumentException("End date must be after the start date.");
            }

            int duration = Period.between(startDate, endDate).getDays();

            if (duration <= 0) {
                throw new IllegalArgumentException("Duration must be greater than 0.");
            }

            packageDTO.setDuration(duration);

            TravelPackage savedPackage = travelPackageRepository.save(convertToEntity(packageDTO));
            return convertToDTO(savedPackage);
        }
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
}