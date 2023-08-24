package com.example.ecommercebackend.api.models;

import jakarta.validation.constraints.*;
import lombok.Getter;

/**
 * @author Mahyar Maleki
 */
 
@Getter
public class RegistrationRequest {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 255)
    private String username;

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 6, max = 32)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,32}$")
    private String password;

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

}
