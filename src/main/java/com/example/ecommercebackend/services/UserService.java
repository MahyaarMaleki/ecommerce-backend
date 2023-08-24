package com.example.ecommercebackend.services;

import com.example.ecommercebackend.api.models.RegistrationRequestBody;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final Validator validator;

    public LocalUser registerUser(RegistrationRequestBody registrationRequestBody) throws UserAlreadyExistsException {
        if(userRepository.findByEmailIgnoreCase(registrationRequestBody.getEmail()).isPresent()
                || userRepository.findByUsernameIgnoreCase(registrationRequestBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setUsername(registrationRequestBody.getUsername());
        user.setEmail(registrationRequestBody.getEmail());
        user.setFirstName(registrationRequestBody.getFirstName());
        user.setLastName(registrationRequestBody.getLastName());
        // TODO: encrypt the password!
        user.setPassword(registrationRequestBody.getPassword());

        return userRepository.save(user);
    }
}
