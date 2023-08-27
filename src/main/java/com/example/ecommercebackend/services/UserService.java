package com.example.ecommercebackend.services;

import com.example.ecommercebackend.api.models.LoginRequest;
import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.exceptions.UserNotVerifiedException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.VerificationToken;
import com.example.ecommercebackend.models.repositories.LocalUserRepository;
import com.example.ecommercebackend.models.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final LocalUserRepository localUserRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EncryptionService encryptionService;

    private final JWTService jwtService;

    private final EmailService emailService;


    public LocalUser registerUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsException, EmailFailureException {
        if(localUserRepository.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent()
                || localUserRepository.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent()) {
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

        return localUserRepository.save(user);
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
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByToken(token);
        if(optionalVerificationToken.isPresent()) {
            VerificationToken verificationToken = optionalVerificationToken.get();
            LocalUser user = verificationToken.getUser();
            if(!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                localUserRepository.save(user);
                user.getVerificationTokens().remove(verificationToken);
                return true;
            }
        }
        return false;
    }

    public String loginUser(LoginRequest loginRequest) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> optionalUser = localUserRepository.findByUsernameIgnoreCase(loginRequest.getUsername());
        if(optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();
            if(encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                if(user.getIsEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend;
                    if(verificationTokens.isEmpty()) {
                        resend = true;
                    } else {
                        resend = verificationTokens.get(0).getCreatedTimestamp().before(
                                new Timestamp(System.currentTimeMillis() - (1000L * 30 * 60))); // half an hour
                    }

                    if(resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

}
