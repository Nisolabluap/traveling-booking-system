package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.PaymentDTO;
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

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@RequestParam String creditCardNum, @RequestParam Long bookingId) {
        PaymentDTO paymentDTO = paymentService.processPayment(creditCardNum, bookingId);
        return ResponseEntity.ok(paymentDTO);
    }
}
