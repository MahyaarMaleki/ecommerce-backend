package com.example.ecommercebackend.services;

import com.example.ecommercebackend.api.models.LoginRequest;
import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.VerificationToken;
import com.example.ecommercebackend.models.repositories.UserRepository;
import com.example.ecommercebackend.models.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EncryptionService encryptionService;

    private final JWTService jwtService;

    private final EmailService emailService;

    private final Validator validator;

    public LocalUser registerUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsException, EmailFailureException {
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

        VerificationToken verificationToken = createVerificationToken(user);
        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(verificationToken);

        return userRepository.save(user);
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);

        return verificationToken;
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
