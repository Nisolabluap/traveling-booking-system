package application.com.orangeteam.exceptions;

public class CustomerCreateException extends RuntimeException {

    public CustomerCreateException(String message) {
        super(message);
    }
}
