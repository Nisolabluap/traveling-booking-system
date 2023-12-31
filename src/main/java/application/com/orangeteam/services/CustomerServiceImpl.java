package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.customer_exceptions.CustomerCreateException;
import application.com.orangeteam.exceptions.customer_exceptions.CustomerNotFoundException;
import application.com.orangeteam.exceptions.validation_exceptions.InvalidEmailFormatException;
import application.com.orangeteam.exceptions.validation_exceptions.InvalidPhoneFormatException;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    private void validateCustomerDto(CustomerDTO customerDTO) {
        String phoneNumber = customerDTO.getPhoneNumber();
        String email = customerDTO.getEmail();

        if (!phoneNumber.matches("^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$")) {
            throw new InvalidPhoneFormatException("Phone number format invalid.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailFormatException("Email format invalid.");
        }
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        validateCustomerDto(customerDTO);

        Customer customer = objectMapper.convertValue(customerDTO, Customer.class);

        try {
            Customer customerRepositoryEntity = customerRepository.save(customer);
            return objectMapper.convertValue(customerRepositoryEntity, CustomerDTO.class);
        } catch (DataIntegrityViolationException exception) {
            log.info("Failed to create a new customer. Email or phone number already in use");
            throw new CustomerCreateException("Email or phone number already in use");
        }
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " does not exist"));
        validateCustomerDto(customerDTO);

        BeanUtils.copyProperties(customerDTO, existingCustomer, "id");
        customerRepository.save(existingCustomer);

        return objectMapper.convertValue(existingCustomer, CustomerDTO.class);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> objectMapper.convertValue(customer, CustomerDTO.class)).toList();
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " does not exist"));
        customerRepository.delete(existingCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {
            return objectMapper.convertValue(existingCustomer, CustomerDTO.class);
        } else {
            throw new CustomerNotFoundException("Customer with id " + id + " does not exist");
        }
    }
}
