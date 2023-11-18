package application.com.orangeteam.controllers;

import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Endpoints for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Create a new customer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    @Operation(
            summary = "Get all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        return ResponseEntity.ok(customerDTOList);
    }

    @Operation(
            summary = "Update a customer by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Customer not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer", required = true) @PathVariable Long id,
            @RequestBody @Valid CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(
            summary = "Delete a customer by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Customer not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCustomer(
            @Parameter(description = "ID of the customer", required = true) @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(id);
    }

    @Operation(
            summary = "Get a customer by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Customer not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(
            @Parameter(description = "ID of the customer", required = true) @PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
}
