package application.com.orangeteam.integration_tests;

import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.entities.Customer;
import application.com.orangeteam.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
class CustomerControllerTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhoneNumber("0756565643");
        customer.setEmail("john@test.com");
        customerRepository.save(customer);
    }


    @Test
    void createCustomerTestValidInput() throws Exception {
        CustomerDTO validCustomer = new CustomerDTO();
        validCustomer.setFirstName("John");
        validCustomer.setLastName("Doe");
        validCustomer.setPhoneNumber("0756565649");
        validCustomer.setEmail("johnTheShark@test.com");

        mockMvc.perform(post("/api/customers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phoneNumber").value("0756565649"))
                .andExpect(jsonPath("$.email").value("johnTheShark@test.com"));
    }

    @Test
    void createCustomerTestInvalidInput() throws Exception {
        CustomerDTO invalidCustomer = new CustomerDTO();
        invalidCustomer.setPhoneNumber("2");
        invalidCustomer.setEmail("ja");

        mockMvc.perform(post("/api/customers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomerTest() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateCustomerValidInputTest() throws Exception {
        CustomerDTO validUpdateCustomer = new CustomerDTO();
        validUpdateCustomer.setFirstName("UpdateFirstName");
        validUpdateCustomer.setLastName("UpdateLastName");
        validUpdateCustomer.setPhoneNumber("0722555666");
        validUpdateCustomer.setEmail("email@myemail.ro");

        mockMvc.perform(put("/api/customers/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateCustomer)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCustomerInvalidInputTest() throws Exception {
        CustomerDTO invalidCustomer = new CustomerDTO();
        invalidCustomer.setFirstName("");
        invalidCustomer.setLastName(".");
        invalidCustomer.setPhoneNumber("0724477822");
        invalidCustomer.setEmail("john@test.com");


        mockMvc.perform(put("/api/customers/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomerTest() throws Exception {
        mockMvc.perform(delete("/api/customers/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNonexistentCustomerTest() throws Exception {
        mockMvc.perform(delete("/api/customers/{id}", 999L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByIdCustomerTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        CustomerDTO customerDTO = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customerDTO.getId()).isEqualTo(1L);
        assertThat(customerDTO.getFirstName()).isEqualTo("John");
        assertThat(customerDTO.getLastName()).isEqualTo("Doe");
        assertThat(customerDTO.getPhoneNumber()).isEqualTo("0756565643");
        assertThat(customerDTO.getEmail()).isEqualTo("john@test.com");
    }

    @Test
    void notFoundByIdCustomerTest() throws Exception {
        mockMvc.perform(get("/api/customers/{id}", 2L))
                .andExpect(status().isNotFound());
    }

}
