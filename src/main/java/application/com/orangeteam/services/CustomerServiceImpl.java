package application.com.orangeteam.services;

import application.com.orangeteam.exceptions.CustomerCreateException;
import application.com.orangeteam.exceptions.CustomerNotFoundException;
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

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = objectMapper.convertValue(customerDTO, Customer.class);
        try {
            Customer customerRepositoryEntity = customerRepository.save(customer);
            log.info("Created customer with id: {}", customerRepositoryEntity.getId());
            return objectMapper.convertValue(customerRepositoryEntity, CustomerDTO.class);
        } catch (DataIntegrityViolationException exception) {
            log.info("Failed to create new customer. Email or phone number already in use");
            throw new CustomerCreateException("Email or phone number already in use");
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> objectMapper.convertValue(customer, CustomerDTO.class))
                .collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with id " + customerDTO.getId() + " does not exist");
        }
        BeanUtils.copyProperties(customerDTO, existingCustomer, "id");
        customerRepository.save(existingCustomer);

        return objectMapper.convertValue(existingCustomer, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            customerRepository.deleteById(id);
        } else {
            throw new CustomerNotFoundException("Customer with id " + id + " does not exist");
        }
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