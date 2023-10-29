package com.orangeteam.services;

import com.orangeteam.models.dtos.BookingDTO;
import com.orangeteam.models.entities.Booking;
import com.orangeteam.models.entities.Customer;
import com.orangeteam.models.entities.Payment;
import com.orangeteam.models.entities.TravelPackage;
import com.orangeteam.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public double getTotalApplyDiscount(double amount, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        double discountAmount = (discountPercentage / 100) * amount;
        return amount - discountAmount;
    }
    public boolean processPayment(BookingDTO bookingDTO) {
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
//      payment.setTotalPayment(bookingDTO.getTravelPackage().getPricePerPerson);
        payment.setDiscount(10);
        paymentRepository.save(payment);
        return true;
    }
}