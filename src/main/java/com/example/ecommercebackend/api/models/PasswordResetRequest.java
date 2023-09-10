package com.example.ecommercebackend.api.models;

import com.example.ecommercebackend.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @ValidPassword
    private String newPassword;
}
