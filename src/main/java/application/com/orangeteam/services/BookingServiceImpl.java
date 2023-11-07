package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.BookingCreateException;
import application.com.orangeteam.exceptions.CustomerNotFoundException;
import application.com.orangeteam.exceptions.DuplicateBookingException;
import application.com.orangeteam.exceptions.TravelPackageNotFoundException;
import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import org.springframework.stereotype.Service;

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
        if (isDuplicate(bookingDTO)) {
            throw new DuplicateBookingException("Duplicate booking detected");
        }
        if (!checkIfAvailableReservations(bookingDTO)) {
            throw new BookingCreateException("Number of travelers exceeds available reservations");
        }

        TravelPackage travelPackage = travelPackageRepository.findById(bookingDTO.getTravelPackageID())
                .orElseThrow(() -> new TravelPackageNotFoundException("Invalid travel package ID"));
        Customer customer = customerRepository.findById(bookingDTO.getCustomerID())
                        .orElseThrow(() -> new CustomerNotFoundException("Invalid customer id."));

        travelPackage.setAvailableReservations(travelPackage.getAvailableReservations() - bookingDTO.getNumTravelers());
        travelPackageRepository.save(travelPackage);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTravelPackage(travelPackage);
        booking.setNumTravelers(bookingDTO.getNumTravelers());
        Booking bookingEntity = bookingRepository.save(booking);
        bookingDTO.setId(bookingEntity.getId());

        return bookingDTO;
    }

    private boolean isDuplicate(BookingDTO bookingDTO) {
        return bookingRepository.existsByCustomerAndTravelPackage(
                customerRepository.getReferenceById(bookingDTO.getCustomerID()),
                travelPackageRepository.getReferenceById(bookingDTO.getTravelPackageID()));
    }

    private boolean checkIfAvailableReservations(BookingDTO bookingDTO) {
        TravelPackage travelPackage = travelPackageRepository.getReferenceById(bookingDTO.getTravelPackageID());
        return bookingDTO.getNumTravelers() <= travelPackage.getAvailableReservations();
    }
}