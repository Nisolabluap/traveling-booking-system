package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.travelpackage_exceptions.DuplicateTravelPackageException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageCreateException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageDeleteException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageNotFoundException;
import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.BookingStatus;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class TravelPackageServiceImpl implements TravelPackageService {

    private final TravelPackageRepository travelPackageRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public TravelPackageServiceImpl(TravelPackageRepository travelPackageRepository, EmailService emailService, ObjectMapper objectMapper) {
        this.travelPackageRepository = travelPackageRepository;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
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
        if (isNotValid(packageDTO)) {
            throw new TravelPackageCreateException("Invalid dates");
        }
        if (isDuplicate(packageDTO)) {
            throw new DuplicateTravelPackageException("Travel package already exists");
        }

        int duration = calculateDuration(packageDTO.getStartingDate(), packageDTO.getEndingDate());
        packageDTO.setDuration(duration);
        TravelPackage savedPackage = travelPackageRepository.save(convertToEntity(packageDTO));

        TravelPackageDTO savedPackageDTO = convertToDTO(savedPackage);
        int savedDuration = calculateDuration(savedPackageDTO.getStartingDate(), savedPackageDTO.getEndingDate());
        savedPackageDTO.setDuration(savedDuration);

        return savedPackageDTO;
    }

    @Override
    public TravelPackageDTO updateTravelPackage(Long id, TravelPackageDTO packageDTO) {
        if (isNotValid(packageDTO)) {
            throw new TravelPackageCreateException("Invalid dates");
        }
        TravelPackage travelPackageOld = travelPackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Travel package with ID " + id + " not found"));

        TravelPackage travelPackageNew = convertToEntity(packageDTO);
        TravelPackageDTO travelPackageResponseDTO = convertToDTO(travelPackageRepository.save(travelPackageNew));
        int duration = calculateDuration(travelPackageResponseDTO.getStartingDate(), travelPackageResponseDTO.getEndingDate());
        travelPackageResponseDTO.setDuration(duration);

        if (!travelPackageOld.getDestination().equals(travelPackageNew.getDestination())
                || !travelPackageOld.getStartingDate().isEqual(travelPackageNew.getStartingDate())
                || !travelPackageOld.getEndingDate().isEqual(travelPackageNew.getStartingDate())) {

            TravelPackageDTO oldTravelPackageDTO = objectMapper.convertValue(travelPackageOld, TravelPackageDTO.class);
            List<Booking> bookings = travelPackageNew.getBookings();
            if (bookings != null) {
                bookings.forEach(booking -> {
                            CustomerDTO customerDTO = objectMapper.convertValue(booking.getCustomer(), CustomerDTO.class);
                            BookingDTO bookingDTO = objectMapper.convertValue(booking, BookingDTO.class);
                            emailService.sendItineraryChangeEmail(customerDTO, bookingDTO, oldTravelPackageDTO, travelPackageResponseDTO);
                        }
                );
            }

            int updatedDuration = calculateDuration(travelPackageResponseDTO.getStartingDate(), travelPackageResponseDTO.getEndingDate());
            travelPackageResponseDTO.setDuration(updatedDuration);
        }
        return travelPackageResponseDTO;
    }

    @Override
    public void deleteTravelPackage(Long id) {
        TravelPackage travelPackage = travelPackageRepository.findById(id)
                .orElseThrow(() -> new TravelPackageNotFoundException("Travel package with id " + id + " not found."));
        Optional<Booking> validBooking = travelPackage.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCELLED)
                .findAny();
        if (validBooking.isPresent()) {
            throw new TravelPackageDeleteException("Cannot delete travel package with bookings");
        }
        travelPackageRepository.delete(travelPackage);
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

    @Override
    public List<TravelPackageDTO> getTravelPackageBetweenDates(LocalDate startingDate, LocalDate endingDate, boolean ascending) {
        List<TravelPackage> filteredPackages;

        if (ascending) {
            filteredPackages = travelPackageRepository.findByStartingDateBetweenOrderByStartingDateAsc(startingDate, endingDate);
        } else {
            filteredPackages = travelPackageRepository.findByStartingDateBetweenOrderByStartingDateDesc(startingDate, endingDate);
        }

        return filteredPackages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<TravelPackageDTO> getTravelPackageWithPriceBetweenTwoValues(double minPrice, double maxPrice) {
        List<TravelPackage> filteredPackages = travelPackageRepository.findByPricePerPersonBeforeDiscountBetween(minPrice, maxPrice);

        return filteredPackages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<TravelPackageDTO> getTravelPackageByDestination(String destination) {
        List<TravelPackage> travelPackages = travelPackageRepository.findByDestination(destination);
        if (travelPackages.isEmpty()) {
            throw new EntityNotFoundException("No travel packages found for destination: " + destination);
        }

        return travelPackages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private boolean isNotValid(TravelPackageDTO travelPackageDTO) {
        return !travelPackageDTO.getStartingDate().isAfter(LocalDate.now()) ||
                !travelPackageDTO.getStartingDate().plusDays(2).isBefore(travelPackageDTO.getEndingDate());
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

    private int calculateDuration(LocalDate startingDate, LocalDate endingDate) {
        Period period = Period.between(startingDate, endingDate);
        int days = period.getDays();
        int months = period.getMonths();
        int years = period.getYears();

        return years * 365 + months * 30 + days;
    }
}