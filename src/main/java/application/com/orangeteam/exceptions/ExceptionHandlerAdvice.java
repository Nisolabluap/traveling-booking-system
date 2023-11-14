package application.com.orangeteam.exceptions;

import application.com.orangeteam.exceptions.booking_exceptions.BookingCreateException;
import application.com.orangeteam.exceptions.booking_exceptions.BookingNotFoundException;
import application.com.orangeteam.exceptions.booking_exceptions.DuplicateBookingException;
import application.com.orangeteam.exceptions.customer_exceptions.CustomerCreateException;
import application.com.orangeteam.exceptions.customer_exceptions.CustomerNotFoundException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.DuplicateTravelPackageException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageCreateException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageDeleteException;
import application.com.orangeteam.exceptions.travelpackage_exceptions.TravelPackageNotFoundException;
import application.com.orangeteam.exceptions.validation_exceptions.InvalidEmailFormatException;
import application.com.orangeteam.exceptions.validation_exceptions.InvalidPhoneFormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private final ObjectMapper objectMapper;

    public ExceptionHandlerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> customerNotFoundException(CustomerNotFoundException customerNotFoundException) {
        return new ResponseEntity<>(objectToString(
                Map.of("message", customerNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(CustomerCreateException.class)
    public ResponseEntity<String> customerCreateException(CustomerCreateException customerCreateException){
        return new ResponseEntity<>(objectToString(
                Map.of("message", customerCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateTravelPackageException.class)
    public ResponseEntity<String> duplicateTravelPackageException(DuplicateTravelPackageException  duplicateTravelPackageException) {
        return new ResponseEntity<>(objectToString(Map.of("message", duplicateTravelPackageException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(TravelPackageCreateException.class)
    public ResponseEntity<String> travelPackageCreateException(TravelPackageCreateException travelPackageCreateException) {
        return new ResponseEntity<>(objectToString(Map.of("message", travelPackageCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(TravelPackageNotFoundException.class)
    public ResponseEntity<String> travelPackageNotFoundException(TravelPackageNotFoundException travelPackageNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", travelPackageNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(TravelPackageDeleteException.class)
    public ResponseEntity<String> travelPackageDeleteException(TravelPackageDeleteException travelPackageDeleteException) {
        return new ResponseEntity<>(objectToString(Map.of("message", travelPackageDeleteException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(BookingCreateException.class)
    public ResponseEntity<String> bookingCreateException(BookingCreateException bookingCreateException) {
        return new ResponseEntity<>(objectToString(Map.of("message", bookingCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> bookingNotFoundException(BookingNotFoundException bookingNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", bookingNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<String> duplicateBookingException(DuplicateBookingException duplicateBookingException) {
        return new ResponseEntity<>(objectToString(Map.of("message", duplicateBookingException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<String> invalidEmailFormatException(InvalidEmailFormatException invalidEmailFormatException) {
        return new ResponseEntity<>(objectToString(Map.of("message", invalidEmailFormatException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(InvalidPhoneFormatException.class)
    public ResponseEntity<String> invalidPhoneFormatException(InvalidPhoneFormatException invalidPhoneFormatException) {
        return new ResponseEntity<>(objectToString(Map.of("message", invalidPhoneFormatException.getMessage())), CONFLICT);
    }

    private String objectToString(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Error processing response to string");
            return "Internal error";
        }
    }
}
