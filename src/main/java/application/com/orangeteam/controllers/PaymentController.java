package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("api/payments")
@Tag(name = "Payment API", description = "Endpoints for managing payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Process payment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(
            @RequestBody @Valid PaymentDTO paymentDTO) {
        PaymentDTO paymentResponseDTO = paymentService.processPayment(
                paymentDTO.getBankAccountInfo(),
                paymentDTO.getBookingID());
        return ResponseEntity.ok(paymentResponseDTO);
    }
}
