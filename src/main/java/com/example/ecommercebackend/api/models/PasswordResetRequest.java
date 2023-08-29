package com.example.ecommercebackend.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mahyar Maleki
 */

@NoArgsConstructor
@Getter
@Setter
public class PasswordResetRequest {

    @NotNull
    @NotBlank
    private String token;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,32}$")
    private String newPassword;
}
