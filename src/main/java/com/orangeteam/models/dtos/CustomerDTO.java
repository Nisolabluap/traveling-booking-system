package com.orangeteam.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CustomerDTO {

    private Long id;

    @NotEmpty(message = "this field must not be empty")
    private String firstName;

    @NotEmpty(message = "this field must not be empty")
    private String lastName;

    @NotEmpty(message = "this field must not be empty")
    private String phoneNumber;

    @Email
    @NotEmpty(message = "this field must not be empty")
    private String email;
}
