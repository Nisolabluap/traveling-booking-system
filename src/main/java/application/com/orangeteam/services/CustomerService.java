package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    CustomerDTO getCustomerById(Long id);
}