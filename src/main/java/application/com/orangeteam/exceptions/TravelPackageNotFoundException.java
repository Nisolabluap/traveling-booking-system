package application.com.orangeteam.exceptions;

public class TravelPackageNotFoundException extends RuntimeException {

    public TravelPackageNotFoundException(String message) {
        super(message);
    }
}
