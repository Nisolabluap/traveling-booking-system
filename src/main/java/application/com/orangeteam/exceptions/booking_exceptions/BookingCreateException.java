package application.com.orangeteam.exceptions.booking_exceptions;

public class BookingCreateException extends RuntimeException {
    public BookingCreateException(String message) {
        super(message);
    }
}
