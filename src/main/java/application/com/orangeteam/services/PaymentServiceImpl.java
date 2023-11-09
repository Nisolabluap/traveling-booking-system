package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.PaymentException;
import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.entities.*;
import application.com.orangeteam.repositories.BookingRepository;
import application.com.orangeteam.services.PaymentService;
import application.com.orangeteam.repositories.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    public double getTotalApplyDiscount(double amount, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        double discountAmount = (discountPercentage / 100) * amount;
        return amount - discountAmount;
    }

    public boolean processPayment(double paymentAmount, Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            double totalAmount = getTotalApplyDiscount(booking.getTotalAmount(), booking.getDiscount());

            if (paymentAmount >= totalAmount) {
                Payment payment = new Payment();
                payment.setPaymentDate(LocalDateTime.now());
                payment.setTotalPayment(totalAmount);
                payment.setBooking(booking);

                paymentRepository.save(payment);
                booking.setPaymentStatus(PaymentStatus.PAID);
                bookingRepository.save(booking);

                return true;
            } else throw new PaymentException("Insufficient payment amount");
        } else {
            throw new EntityNotFoundException("Booking not found with ID: " + bookingId);
        }
    }
}