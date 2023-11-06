package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final CustomerRepository customerRepository;
    private final TravelPackageRepository travelPackageRepository;
    private final ObjectMapper objectMapper;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(CustomerRepository customerRepository, TravelPackageRepository travelPackageRepository, ObjectMapper objectMapper, BookingRepository bookingRepository) {
        this.customerRepository = customerRepository;
        this.travelPackageRepository = travelPackageRepository;
        this.objectMapper = objectMapper;
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
}