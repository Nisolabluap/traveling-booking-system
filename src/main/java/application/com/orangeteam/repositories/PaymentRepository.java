package application.com.orangeteam.repositories;

import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBooking(Booking booking);
}