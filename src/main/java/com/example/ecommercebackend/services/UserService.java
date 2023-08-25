package com.example.ecommercebackend.services;

import com.example.ecommercebackend.api.models.LoginRequest;
import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final JWTService jwtService;

    private final Validator validator;

    public LocalUser registerUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsException {
        if(userRepository.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent()
                || userRepository.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPassword(encryptionService.encryptPassword(registrationRequest.getPassword()));

        return userRepository.save(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        Optional<LocalUser> optionalUser = userRepository.findByUsernameIgnoreCase(loginRequest.getUsername());
        if(optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();
            if(encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }

        return null;
    }

}
