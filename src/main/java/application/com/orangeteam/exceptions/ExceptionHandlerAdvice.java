package application.com.orangeteam.exceptions;

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

    private String objectToString(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Error processing response to string");
            return "Internal error";
        }
    }
}
