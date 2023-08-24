package com.example.ecommercebackend.api.controllers.auth;

import com.example.ecommercebackend.api.models.RegistrationRequestBody;
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
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationRequestBody registrationRequestBody) {
        try {
            LocalUser user = userService.registerUser(registrationRequestBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
