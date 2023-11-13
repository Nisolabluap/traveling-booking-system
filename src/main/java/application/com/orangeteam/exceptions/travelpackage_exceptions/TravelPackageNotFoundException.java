package application.com.orangeteam.exceptions.travelpackage_exceptions;

public class TravelPackageNotFoundException extends RuntimeException {

    public TravelPackageNotFoundException(String message) {
        super(message);
    }
}
