package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, EmailService emailService, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = objectMapper.convertValue(customerDTO, Customer.class);

        Customer customerRepositoryEntity = customerRepository.save(customer);
        log.info("Created customer with id: {}", customerRepositoryEntity.getId());
        CustomerDTO customerReturnDTO = objectMapper.convertValue(customerRepositoryEntity, CustomerDTO.class);
        emailService.sendWelcomeEmail(customerReturnDTO);

        return customerReturnDTO;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> objectMapper.convertValue(customer, CustomerDTO.class))
                .toList();

        return customerDTOS;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existongCustomer = customerRepository.findById(id).orElse(null);
        if (existongCustomer == null) {
            return null;
        }
        BeanUtils.copyProperties(customerDTO, existongCustomer, "id");
        customerRepository.save(existongCustomer);

        return objectMapper.convertValue(existongCustomer, CustomerDTO.class);
    }

    @Override
    public Boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {
            return objectMapper.convertValue(existingCustomer, CustomerDTO.class);
        }
        return null;
    }
}