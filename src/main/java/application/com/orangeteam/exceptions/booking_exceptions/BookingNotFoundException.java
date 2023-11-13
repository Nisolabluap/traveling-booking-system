package application.com.orangeteam.exceptions.booking_exceptions;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String message) {
        super(message);
    }
}
