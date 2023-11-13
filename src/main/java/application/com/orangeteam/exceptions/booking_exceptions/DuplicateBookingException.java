package application.com.orangeteam.exceptions.booking_exceptions;

public class DuplicateBookingException extends RuntimeException{

    public DuplicateBookingException(String message) {
        super(message);
    }
}
