package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody @Valid PaymentDTO paymentDTO) {
        PaymentDTO paymentResponseDTO = paymentService.processPayment(
                paymentDTO.getBankAccountInfo(),
                paymentDTO.getBookingID());
        return ResponseEntity.ok(paymentResponseDTO);
    }
}
