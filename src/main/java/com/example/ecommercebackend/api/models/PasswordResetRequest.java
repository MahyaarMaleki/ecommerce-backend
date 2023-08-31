package com.example.ecommercebackend.api.models;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "JWT token")
    @NotNull
    @NotBlank
    private String token;

    @Schema(title = "Password",example = "Password!123", minLength = 6, maxLength = 32,
            description = "Password must contain at least one lowercase letter, one uppercase letter, one number and one special character.")
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,32}$")
    private String newPassword;
}
