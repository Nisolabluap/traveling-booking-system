package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.booking_exceptions.BookingCreateException;
import application.com.orangeteam.exceptions.booking_exceptions.BookingNotFoundException;
import application.com.orangeteam.exceptions.booking_exceptions.BookingUpdateException;
import application.com.orangeteam.exceptions.booking_exceptions.DuplicateBookingException;
import application.com.orangeteam.exceptions.customer_exceptions.CustomerNotFoundException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageNotFoundException;
import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.entities.*;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.CustomerRepository;
import application.com.orangeteam.repositories.PaymentRepository;
import application.com.orangeteam.repositories.TravelPackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final CustomerRepository customerRepository;
    private final TravelPackageRepository travelPackageRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public BookingServiceImpl(
            CustomerRepository customerRepository,
            TravelPackageRepository travelPackageRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            PaymentService paymentService, EmailService emailService, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.travelPackageRepository = travelPackageRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        if (isDuplicate(bookingDTO)) {
            throw new DuplicateBookingException("Duplicate booking detected. Use update to modify booking.");
        }
        if (!checkIfAvailableReservations(bookingDTO.getNumTravelers(), bookingDTO.getTravelPackageID())) {
            throw new BookingCreateException("Number of travelers exceeds available reservations");
        }

        TravelPackage travelPackage = travelPackageRepository.findById(bookingDTO.getTravelPackageID())
                .orElseThrow(() -> new TravelPackageNotFoundException("Invalid travel package ID"));
        Customer customer = customerRepository.findById(bookingDTO.getCustomerID())
                .orElseThrow(() -> new CustomerNotFoundException("Invalid customer id."));
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setEmail(customer.getFirstName());

        travelPackage.setAvailableReservations(travelPackage.getAvailableReservations() - bookingDTO.getNumTravelers());
        travelPackage = travelPackageRepository.save(travelPackage);

        double priceTotal = calculateTotal(bookingDTO.getNumTravelers(),
                travelPackage.getPricePerPersonBeforeDiscount(),
                travelPackage.getDiscountPercent());

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTravelPackage(travelPackage);
        booking.setNumTravelers(bookingDTO.getNumTravelers());
        booking.setPriceTotal(priceTotal);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setCreatedAt(LocalDateTime.now());
        Booking bookingEntity = bookingRepository.save(booking);

        BookingDTO bookingResponseDTO = convertToDTO(bookingEntity);

        emailService.sendBookingConfirmationEmail(customerDTO, bookingResponseDTO);

        return bookingResponseDTO;
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
            bookingDTOList.add(convertToDTO(bookingEntity));
        }
        return bookingDTOList;
    }

    @Override
    public List<BookingDTO> getBookingsByTravelPackage(Long travelPackageId) {
        List<Booking> bookingEntityList = bookingRepository.findByTravelPackage(travelPackageRepository.findById(travelPackageId)
                .orElseThrow(() -> new TravelPackageNotFoundException("Cannot find travel package with id " + travelPackageId)));
        return getBookingDTOS(bookingEntityList);
    }

    @Override
    public List<BookingDTO> getBookingsByDestination(String destination) {
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
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        return convertToDTO(booking);
    }

    @Override
    public BookingDTO updateNumTravelers(Long id, Integer numTravelers) {
        Booking bookingToBeUpdated = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking wit id " + id + " not found."));

        TravelPackage travelPackage = travelPackageRepository.findById(bookingToBeUpdated.getTravelPackage().getId()).get();

        if (bookingToBeUpdated.getBookingStatus() != BookingStatus.BOOKED) {
            throw new BookingUpdateException("Cannot modify payed or canceled booking");
        }
        int travelersModifier = numTravelers - bookingToBeUpdated.getNumTravelers();
        if (travelersModifier > 0) {
            if (checkIfAvailableReservations(travelersModifier, travelPackage.getId())) {
                bookingToBeUpdated.setNumTravelers(numTravelers);
            } else {
                throw new BookingCreateException("Number of travelers exceeds available reservations");
            }
        }

        travelPackage.setAvailableReservations(travelPackage.getAvailableReservations() + travelersModifier);
        travelPackageRepository.save(travelPackage);

        bookingToBeUpdated.setNumTravelers(numTravelers);
        bookingToBeUpdated.setPriceTotal(calculateTotal(numTravelers,
                travelPackage.getPricePerPersonBeforeDiscount(),
                travelPackage.getDiscountPercent()));
        Booking updatedBooking = bookingRepository.save(bookingToBeUpdated);

        return convertToDTO(updatedBooking);
    }

    @Override
    public BookingDTO cancel(Long id) {
        Booking bookingToBeCanceled = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        if (bookingToBeCanceled.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new DuplicateBookingException("Booking already canceled");
        }

        TravelPackage travelPackage = bookingToBeCanceled.getTravelPackage();
        travelPackage.setAvailableReservations(travelPackage.getAvailableReservations() + bookingToBeCanceled.getNumTravelers());
        travelPackageRepository.save(travelPackage);

        if (bookingToBeCanceled.getBookingStatus() == BookingStatus.PAID) {
            Payment successfulPayment = paymentRepository.findByBooking(bookingToBeCanceled).stream()
                    .filter(payment -> payment.getPaymentStatus() == PaymentStatus.SUCCESSFUL)
                    .findFirst().get();
            paymentService.reimburse(successfulPayment.getId());
            successfulPayment.setPaymentStatus(PaymentStatus.REIMBURSED);
            paymentRepository.save(successfulPayment);

        }

        bookingToBeCanceled.setBookingStatus(BookingStatus.CANCELLED);
        Booking canceledBooking = bookingRepository.save(bookingToBeCanceled);

        return convertToDTO(canceledBooking);
    }

    private boolean checkIfAvailableReservations(int numTravelers, Long travelPackageId) {
        TravelPackage travelPackage = travelPackageRepository.getReferenceById(travelPackageId);
        return numTravelers <= travelPackage.getAvailableReservations();
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerID(booking.getCustomer().getId());
        bookingDTO.setTravelPackageID(booking.getTravelPackage().getId());
        bookingDTO.setNumTravelers(booking.getNumTravelers());
        bookingDTO.setPriceTotal(booking.getPriceTotal());
        bookingDTO.setBookingStatus(booking.getBookingStatus());
        return bookingDTO;
    }

    private double calculateTotal(int numTravelers, double pricePerPersonBeforeDiscount, int discountPercent) {
        double totalBeforeDiscount = numTravelers * pricePerPersonBeforeDiscount;
        double discountAmount = totalBeforeDiscount * discountPercent / 100;
        return totalBeforeDiscount - discountAmount;
    }

    private boolean isDuplicate(BookingDTO bookingDTO) {
        return bookingRepository.existsByCustomerAndTravelPackage(
                customerRepository.findById(bookingDTO.getCustomerID())
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found")),
                travelPackageRepository.findById(bookingDTO.getTravelPackageID())
                        .orElseThrow(() -> new TravelPackageNotFoundException("Travel package not found")));

    }

    @Scheduled(fixedDelay = 60 * 3 * 1000)
    public void cancelExpiredBookings() {
        List<Booking> expiredBookings = bookingRepository.findByBookingStatusAndCreatedAtBefore(BookingStatus.BOOKED, LocalDateTime.now().minusMinutes(3));
        expiredBookings.forEach(booking -> cancel(booking.getId()));
    }
}