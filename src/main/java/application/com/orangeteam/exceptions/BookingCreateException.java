package application.com.orangeteam.exceptions;

public class BookingCreateException extends RuntimeException {
    public BookingCreateException(String message) {
        super(message);
    }
}
