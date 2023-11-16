package application.com.orangeteam.integration_tests;

import application.com.orangeteam.models.dtos.TravelPackageDTO;
import application.com.orangeteam.models.entities.TravelPackage;
import application.com.orangeteam.repositories.TravelPackageRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TravelPackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @BeforeEach
    void setUp() {
        TravelPackageDTO travelPackageDTO = new TravelPackageDTO();
        travelPackageDTO.setId(1L);
        travelPackageDTO.setDestination("Test Destination");

        TravelPackage convertedPackage = convertDTOToEntity(travelPackageDTO);

        travelPackageRepository.save(convertedPackage);
    }

    @Test
    void getAllTravelPackagesShouldPass() throws Exception {
        mockMvc.perform(get("/api/travel-packages"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createTravelPackageWithInvalidDatesShouldFail() throws Exception {
        String jsonPayload = "{"
                + "\"name\": \"Example Travel Package\","
                + "\"destination\": \"Example Destination\","
                + "\"description\": \"This is an example travel package description.\","
                + "\"availableReservations\": 10,"
                + "\"pricePerPersonBeforeDiscount\": 1000.0,"
                + "\"discountPercent\": 10,"
                + "\"startingDate\": \"2024-01-01\","
                + "\"endingDate\": \"2023-01-10\""
                + "}";

        mockMvc.perform(post("/api/travel-packages")
                        .contentType("application/json")
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateTravelPackageShouldPass() throws Exception {
        String jsonPayload = "{"
                + "\"name\": \"Updated Test Package\","
                + "\"destination\": \"Updated Destination\","
                + "\"description\": \"Updated description\","
                + "\"availableReservations\": 10,"
                + "\"pricePerPersonBeforeDiscount\": 1000.0,"
                + "\"discountPercent\": 10,"
                + "\"startingDate\": \"2024-01-01\","
                + "\"endingDate\": \"2024-01-10\""
                + "}";

        mockMvc.perform(put("/api/travel-packages/1")
                        .contentType("application/json")
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getTravelPackagesBetweenDatesShouldPass() throws Exception {
        mockMvc.perform(get("/api/travel-packages/between-dates")
                        .param("startingDate", "2023-01-01")
                        .param("endingDate", "2023-01-10")
                        .param("ascending", "true"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getTravelPackagesByDestinationShouldFail() throws Exception {
        mockMvc.perform(get("/api/travel-packages/by-destination")
                        .param("destination", "Test aDestination"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private TravelPackage convertDTOToEntity(TravelPackageDTO travelPackageDTO) {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setName(travelPackageDTO.getName());
        travelPackage.setDestination(travelPackageDTO.getDestination());
        return travelPackage;
    }
}
