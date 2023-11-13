package application.com.orangeteam.exceptions.validation_exceptions;

public class InvalidEmailFormatException extends RuntimeException {

    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
