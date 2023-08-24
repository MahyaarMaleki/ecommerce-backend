package com.example.ecommercebackend.api.controllers.auth;

import com.example.ecommercebackend.api.models.LoginRequest;
import com.example.ecommercebackend.api.models.LoginResponse;
import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mahyar Maleki
 */

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            LocalUser user = userService.registerUser(registrationRequest);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = userService.loginUser(loginRequest);
        if(jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse loginResponse = new LoginResponse(jwt);
            return ResponseEntity.ok(loginResponse);
        }
    }
}
