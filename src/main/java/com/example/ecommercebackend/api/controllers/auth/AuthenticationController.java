package com.example.ecommercebackend.api.controllers.auth;

import com.example.ecommercebackend.api.models.*;
import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.exceptions.UserNotVerifiedException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.services.UserService;
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
public class AuthenticationController {
    private final UserService userService;

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

    @PostMapping(path = "/verify")
    public ResponseEntity<VerificationResponse> verifyUser(@RequestParam String token) {
        if(userService.verifyUser(token)) {
            return ResponseEntity.ok(new VerificationResponse(true, "VERIFIED_SUCCESSFULLY"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

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
            LoginResponse loginResponse = new LoginResponse(jwt, false, responseMessage);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse(jwt, false, "FAILED_TO_SEND_EMAIL"));
        }
        if(jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(jwt, false, "USER_DOES_NOT_EXIST"));
        } else {
            return ResponseEntity.ok(new LoginResponse(jwt, true, "LOGGED_IN_SUCCESSFULLY"));
        }
    }

    @GetMapping(path = "/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;
    }
}
