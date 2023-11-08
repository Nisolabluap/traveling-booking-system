package application.com.orangeteam.exceptions;

public class InvalidPhoneFormatException extends RuntimeException {

    public InvalidPhoneFormatException(String message) {
        super(message);
    }
}
