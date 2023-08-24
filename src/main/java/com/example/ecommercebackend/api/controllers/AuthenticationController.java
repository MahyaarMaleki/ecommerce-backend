package com.example.ecommercebackend.api.controllers;

import com.example.ecommercebackend.api.models.RegistrationBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mahyar Maleki
 */

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @PostMapping(path = "/register")
    public void registerUser(@RequestBody RegistrationBody registrationBody) {

    }
}
