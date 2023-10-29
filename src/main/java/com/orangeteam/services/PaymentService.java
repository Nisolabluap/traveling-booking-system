package com.orangeteam.services;

import com.orangeteam.models.dtos.BookingDTO;

public interface PaymentService {
    boolean processPayment(BookingDTO bookingDTO);
}