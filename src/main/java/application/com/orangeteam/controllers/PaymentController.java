package application.com.orangeteam.controllers;

import application.com.orangeteam.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("api/payments")
public class PaymentController {

  private PaymentService paymentService;
    @PostMapping
    public ResponseEntity<String> processPayment(@RequestParam double paymentAmount, @RequestParam Long bookingId) {
            boolean paymentProcessed = paymentService.processPayment(paymentAmount, bookingId);
            return ResponseEntity.ok("Payment processed successfully");
}
}
