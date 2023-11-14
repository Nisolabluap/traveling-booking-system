package application.com.orangeteam.exceptions.payment_exceptions;

public class PaymentStatusNotBookedException extends RuntimeException{
    public PaymentStatusNotBookedException(String message){
        super(message);
    }
}
