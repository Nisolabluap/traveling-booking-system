package application.com.orangeteam.exceptions;

public class DuplicateTravelPackageException extends RuntimeException {

    public DuplicateTravelPackageException(String message) {
        super(message);
    }
}
