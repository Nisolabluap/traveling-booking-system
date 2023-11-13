package application.com.orangeteam.exceptions.validation_exceptions;

public class InvalidPhoneFormatException extends RuntimeException {

    public InvalidPhoneFormatException(String message) {
        super(message);
    }
}
