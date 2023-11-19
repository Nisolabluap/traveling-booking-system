package application.com.orangeteam.repositories;

import application.com.orangeteam.models.entities.Booking;
import application.com.orangeteam.models.enums.BookingStatus;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.models.entities.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByCustomerAndTravelPackage(Customer customer, TravelPackage travelPackage);

    List<Booking> findByCustomer(Customer customer);

    List<Booking> findByTravelPackage(TravelPackage travelPackage);

    List<Booking> findByBookingStatusAndCreatedAtBefore(BookingStatus bookingStatus, LocalDateTime twentyFourHoursAgo);
}