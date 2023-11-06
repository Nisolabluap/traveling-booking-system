package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.Destination;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomer(new Customer());
    }

    @Override
    public List<Booking> getBookingsByTravelPackage(Long travelPackageId) {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(travelPackageId);
        return bookingRepository.findByTravelPackage(travelPackage);
    }
    @Override
    public List<Booking> getBookingsByDestination(Long destinationId){
        Destination destination = new Destination();
        List<Booking> bookings = bookingRepository.findByDestination(destination);
       destination.setId(destinationId);

       return bookingRepository.findByDestination(destination);

    }
}