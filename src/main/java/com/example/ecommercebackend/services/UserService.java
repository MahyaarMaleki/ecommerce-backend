package com.example.ecommercebackend.services;

import com.example.ecommercebackend.api.models.LoginRequest;
import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.exceptions.UserNotVerifiedException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.VerificationToken;
import com.example.ecommercebackend.models.DAOs.LocalUserDAO;
import com.example.ecommercebackend.models.DAOs.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final LocalUserDAO localUserDAO;

    private final VerificationTokenDAO verificationTokenDAO;

    private final EncryptionService encryptionService;

    private final JWTService jwtService;

    @Value("${jwt.verification.expiryInSeconds}")
    private int verificationJwtExpiryInSeconds;

    private final EmailService emailService;

    private final Validator validator;

    public LocalUser registerUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsException, EmailFailureException {
        if(localUserDAO.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent()
                || localUserDAO.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPassword(encryptionService.encryptPassword(registrationRequest.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);

        return localUserDAO.save(user);
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);

        return verificationToken;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenDAO.findByToken(token);
        if(optionalVerificationToken.isPresent()) {
            VerificationToken verificationToken = optionalVerificationToken.get();
            LocalUser user = verificationToken.getUser();
            if(!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public String loginUser(LoginRequest loginRequest) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> optionalUser = localUserDAO.findByUsernameIgnoreCase(loginRequest.getUsername());
        if(optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();
            if(encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                if(user.getIsEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty()
                            | verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (1000L * verificationJwtExpiryInSeconds)));

                    if(resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

}
