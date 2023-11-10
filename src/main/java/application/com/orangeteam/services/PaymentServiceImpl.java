package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.BookingNotFoundException;
import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.BookingStatus;
import application.com.orangeteam.models.entities.Payment;
import application.com.orangeteam.models.entities.PaymentStatus;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.repositories.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final Random random;
    private final ObjectMapper objectMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository, Random random, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.random = random;
        this.objectMapper = objectMapper;
    }

    public PaymentDTO processPayment(String creditCardNumber, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + bookingId + "not found"));

        double price = booking.getPriceTotal();

        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTotalAmount(price);
        payment.setBooking(booking);

        if (makePayment(price, creditCardNumber)) {
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            booking.setBookingStatus(BookingStatus.PAID);
            bookingRepository.save(booking);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }
        paymentRepository.save(payment);

        return objectMapper.convertValue(payment, PaymentDTO.class);
    }

    private boolean makePayment(double amount, String cardNumber) {
        log.info("Attempted payment of " + amount + "from card ".concat(cardNumber));
        return random.nextInt(10) < 8;
    }
}

