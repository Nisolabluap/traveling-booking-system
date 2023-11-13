package application.com.orangeteam.exceptions.customer_exceptions;

public class CustomerCreateException extends RuntimeException {

    public CustomerCreateException(String message) {
        super(message);
    }
}
