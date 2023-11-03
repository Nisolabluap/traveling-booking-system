package application.com.orangeteam.controllers;


import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Validated CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        return ResponseEntity.ok(customerDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
}