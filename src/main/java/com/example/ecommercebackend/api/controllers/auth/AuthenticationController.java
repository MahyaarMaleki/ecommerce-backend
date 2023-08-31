package com.example.ecommercebackend.api.controllers.auth;

import com.example.ecommercebackend.api.models.*;
import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.exceptions.EmailNotFoundException;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.exceptions.UserNotVerifiedException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mahyar Maleki
 */

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
@Tag(name = "authentication")
public class AuthenticationController {
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Register a new user by providing a valid registration request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "User with the specified username or email already exists.", content = @Content),
            @ApiResponse(responseCode = "400", description = "All the fields must be valid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send verification email.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Registered successfully.")
    })
    @PostMapping(path = "/register")
    public ResponseEntity<RegistrationResponse> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            LocalUser user = userService.registerUser(registrationRequest);
            return ResponseEntity.ok(new RegistrationResponse(true, "REGISTERED_SUCCESSFULLY"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegistrationResponse(false, "USER_ALREADY_EXISTS"));
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegistrationResponse(false, "FAILED_TO_SEND_EMAIL"));
        }
    }

    @Operation(summary = "Verify a newly registered user", description = "Verifies the newly registered user to be able to login by verification token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Provided verification token does not exist or the user is already verified.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Verified successfully.")
    })
    @PostMapping(path = "/verify")
    public ResponseEntity<VerificationResponse> verifyUser(@RequestParam String token) {
        if(userService.verifyUser(token)) {
            return ResponseEntity.ok(new VerificationResponse(true, "VERIFIED_SUCCESSFULLY"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Login with an existing user", description = "Login with an existing user by providing their username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "User is not verified.", content = @Content),
            @ApiResponse(responseCode = "400", description = "User does not exist or at least one field is invalid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to resend verification email.", content = @Content),
            @ApiResponse(responseCode = "200", description = "logged in successfully.")
    })
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = null;
        try {
            jwt = userService.loginUser(loginRequest);
        } catch (UserNotVerifiedException e) {
            String responseMessage = "USER_NOT_VERIFIED";
            if(e.isNewEmailSent()) {
                responseMessage += "_EMAIL_RESENT";
            }
            LoginResponse loginResponse = new LoginResponse(false, responseMessage, jwt);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse(false, "FAILED_TO_SEND_EMAIL", jwt));
        }
        if(jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(false, "USER_DOES_NOT_EXIST", jwt));
        } else {
            return ResponseEntity.ok(new LoginResponse(true, "LOGGED_IN_SUCCESSFULLY", jwt));
        }
    }

    @Operation(summary = "Get currently logged in user's info")
    @ApiResponse(responseCode = "403", description = "You need to login first.")
    @GetMapping(path = "/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;
    }

    @Operation(summary = "Request a forgot password operation", description = "Request a forgot password operation by providing user's email to receive password reset token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "User with specified email does not exist.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send email.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Operation was successfully.")
    })
    @PostMapping(path = "/forgot")
    public ResponseEntity<BaseResponse> forgotPassword(@RequestParam String email) {
        try {
            userService.forgotPassword(email);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(true, "EMAIL_SENT_SUCCESSFULLY"));
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(false, "EMAIL_NOT_FOUND"));
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(false, "FAILED_TO_SEND_EMAIL"));
        }
    }

    @Operation(summary = "Reset a user's password", description = "Reset a user's password by providing the reset password token and a valid password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "User with specified email does not exist or at least one field is invalid.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send email.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Password changed successfully.")
    })
    @PostMapping(path = "/reset")
    public ResponseEntity<BaseResponse> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            userService.resetPassword(passwordResetRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(true, "PASSWORD_CHANGED_SUCCESSFULLY"));
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(false, "EMAIL_NOT_FOUND"));
        }
    }
}
