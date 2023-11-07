package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.CustomerNotFoundException;
import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final CustomerRepository customerRepository;
    private final TravelPackageRepository travelPackageRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(CustomerRepository customerRepository, TravelPackageRepository travelPackageRepository, BookingRepository bookingRepository) {
        this.customerRepository = customerRepository;
        this.travelPackageRepository = travelPackageRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        if (bookingRepository.existsByCustomerAndTravelPackage(
                customerRepository.getReferenceById(bookingDTO.getCustomerID()),
                travelPackageRepository.getReferenceById(bookingDTO.getTravelPackageID()))) {
            throw new DuplicateBookingException("Duplicate booking detected");
        } else {
            Booking booking = new Booking();
            Customer customer = customerRepository.getReferenceById(bookingDTO.getCustomerID());
            TravelPackage travelPackage = travelPackageRepository.getReferenceById(bookingDTO.getTravelPackageID());
            booking.setCustomer(customer);
            booking.setTravelPackage(travelPackage);
            booking.setNumTravelers(bookingDTO.getNumTravelers());
            Booking bookingEntity = bookingRepository.save(booking);
            bookingDTO.setId(bookingEntity.getId());
            return bookingDTO;
        }
    }

    public class DuplicateBookingException extends RuntimeException {
        public DuplicateBookingException(String message) {
            super(message);
        }
    }

    @Override
    public List<BookingDTO> getBookingsByCustomer(Long customerId) {
        List<Booking> bookingEntityList = bookingRepository.findByCustomer(customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cannot find customer with " + customerId)));
        return getBookingDTOS(bookingEntityList);
    }

    @NotNull
    private static List<BookingDTO> getBookingDTOS(List<Booking> bookingEntityList) {
        List<BookingDTO> bookingDTOList = new ArrayList<>();

        for (Booking bookingEntity : bookingEntityList) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(bookingEntity.getId());
            bookingDTO.setCustomerID(bookingEntity.getCustomer().getId());
            bookingDTO.setTravelPackageID(bookingEntity.getTravelPackage().getId());
            bookingDTO.setNumTravelers(bookingEntity.getNumTravelers());
            bookingDTOList.add(bookingDTO);
        }
        return bookingDTOList;
    }

    @Override
    public List<BookingDTO> getBookingsByTravelPackage(Long travelPackageId) {
        List<Booking> bookingEntityList = bookingRepository.findByTravelPackage(travelPackageRepository.findById(travelPackageId)
                .orElseThrow(() -> new RuntimeException("Cannot find travel package with id " + travelPackageId)));
        return getBookingDTOS(bookingEntityList);
    }
    @Override
    public List<BookingDTO> getBookingsByDestination(String destination){
        List<TravelPackage> travelPackages = travelPackageRepository.findByDestination(destination);
        List<Booking> allBookingsWithDestination = new ArrayList<>();
        for (TravelPackage travelPackage : travelPackages) {
            List<Booking> bookingsWithTravelPackage = bookingRepository.findByTravelPackage(travelPackage);
            allBookingsWithDestination.addAll(bookingsWithTravelPackage);
        }
       return getBookingDTOS(allBookingsWithDestination);
    }
}