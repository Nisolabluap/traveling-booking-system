package application.com.orangeteam.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Orange Team: Travel Booking System",
                version = "1.0",
                description = "Build a Travel Booking System for a travel agency. " +
                        "The agency offers various travel packages and services to customers looking to plan their vacations. " +
                        "The system should handle package bookings, payment processing, and itinerary management. " +
                        "For more details, please refer to the [project documentation](https://github.com/Nisolabluap/traveling-booking-system#readme).",
                contact = @Contact(
                        name = "Orange Team",
                        email = "orange@team.com",
                        url = "https://github.com/Nisolabluap/traveling-booking-system"
                ),
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/"
                )
        }
)
public class OpenApiConfig {

}
