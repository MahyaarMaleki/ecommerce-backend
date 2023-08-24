package com.example.ecommercebackend.api.models;

/**
 * @author Mahyar Maleki
 */
 
 
public record RegistrationBody(
        String username,
        String email,
        String password,
        String firstName,
        String lastName) {
}
