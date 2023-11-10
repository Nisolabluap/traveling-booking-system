package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.BookingCreateException;
import application.com.orangeteam.exceptions.CustomerNotFoundException;
import application.com.orangeteam.exceptions.DuplicateBookingException;
import application.com.orangeteam.exceptions.TravelPackageNotFoundException;
import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.BookingStatus;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final CustomerRepository customerRepository;
    private final TravelPackageRepository travelPackageRepository;
    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;

    public BookingServiceImpl(CustomerRepository customerRepository, TravelPackageRepository travelPackageRepository, BookingRepository bookingRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.travelPackageRepository = travelPackageRepository;
        this.bookingRepository = bookingRepository;
        this.objectMapper = objectMapper;
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

        double priceTotal = calculateTotal(bookingDTO, travelPackage.getPricePerPersonBeforeDiscount(), travelPackage.getDiscountPercent());

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTravelPackage(travelPackage);
        booking.setNumTravelers(bookingDTO.getNumTravelers());
        booking.setPriceTotal(priceTotal);
        booking.setBookingStatus(BookingStatus.PENDING);
        Booking bookingEntity = bookingRepository.save(booking);

        return objectMapper.convertValue(bookingEntity, BookingDTO.class);
    }

    private double calculateTotal(BookingDTO bookingDTO, double pricePerPersonBeforeDiscount, int discountPercent) {
        double totalBeforeDiscount = bookingDTO.getNumTravelers() * pricePerPersonBeforeDiscount;
        double discountAmount = totalBeforeDiscount - (totalBeforeDiscount * discountPercent / 100);
        return  totalBeforeDiscount - discountAmount;
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

    @Override
    public List<BookingDTO> getBookingsByCustomer(Long customerId) {
        List<Booking> bookingEntityList = bookingRepository.findByCustomer(customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cannot find customer with " + customerId)));
        return getBookingDTOS(bookingEntityList);
    }

    @NotNull
    private List<BookingDTO> getBookingDTOS(List<Booking> bookingEntityList) {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for (Booking bookingEntity : bookingEntityList) {
            bookingDTOList.add(objectMapper.convertValue(bookingEntity, BookingDTO.class));
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

    @Override
    public BookingDTO getBookingById(Long id) {
        return objectMapper.convertValue(bookingRepository.findById(id), BookingDTO.class);
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        return null;
    }

    @Override
    public BookingDTO cancel(Long id) {
        return null;
    }
}