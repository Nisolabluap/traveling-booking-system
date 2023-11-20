package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.booking_exceptions.BookingNotFoundException;
import application.com.orangeteam.exceptions.payment_exceptions.PaymentStatusNotBookedException;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.entities.*;
import application.com.orangeteam.models.enums.BookingStatus;
import application.com.orangeteam.models.enums.PaymentStatus;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final Random random = new Random();
    private final EmailService emailService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository, EmailService emailService, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    public PaymentDTO processPayment(String creditCardNumber, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + bookingId + "not found"));
        if (booking.getBookingStatus() == BookingStatus.PAID) {
            throw new PaymentStatusNotBookedException("Booking already payed.");
        } else if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new PaymentStatusNotBookedException("Booking is canceled.");
        }

        Customer customer = booking.getCustomer();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setEmail(customer.getEmail());

        double price = booking.getPriceTotal();

        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
        payment.setBankAccountInfo(creditCardNumber);
        payment.setTotalAmount(price);
        payment.setBooking(booking);

        boolean paymentSuccessful = makePayment(price, creditCardNumber);
        if (paymentSuccessful) {
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            booking.setBookingStatus(BookingStatus.PAID);
            bookingRepository.save(booking);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }

        Payment paymentEntity = paymentRepository.save(payment);
        PaymentDTO paymentResponseDTO = new PaymentDTO();
        paymentResponseDTO.setId(paymentEntity.getId());
        paymentResponseDTO.setPaymentDateTime(paymentEntity.getPaymentDate());
        paymentResponseDTO.setAmount(paymentEntity.getTotalAmount());
        paymentResponseDTO.setBookingID(paymentEntity.getBooking().getId());
        paymentResponseDTO.setBankAccountInfo(paymentEntity.getBankAccountInfo());
        paymentResponseDTO.setPaymentStatus(paymentEntity.getPaymentStatus());

        if (paymentSuccessful) {
            emailService.sendPaymentReceiptEmail(
                    customerDTO, paymentResponseDTO);
        } else {
            emailService.sendPaymentFailedEmail(customerDTO, paymentResponseDTO);
        }

        return paymentResponseDTO;
    }

    public void reimburse(Long paymentID) {
        Payment payment = paymentRepository.findById(paymentID).get();
        log.info("Reimbursing " + payment.getTotalAmount() + "to card " + payment.getBankAccountInfo());
        payment.setPaymentStatus(PaymentStatus.REIMBURSED);
        paymentRepository.save(payment);
    }

    private boolean makePayment(double amount, String cardNumber) {
        log.info("Attempted payment of " + amount + "from card ".concat(cardNumber));
        return random.nextInt(10) < 8;
    }
}

