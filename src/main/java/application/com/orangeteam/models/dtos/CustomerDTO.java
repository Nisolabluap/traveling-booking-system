package application.com.orangeteam.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "this field must not be empty")
    private String firstName;

    @NotBlank(message = "this field must not be empty")
    private String lastName;

    private String phoneNumber;

    private String email;
}