package com.example.ecommercebackend.services;

import com.example.ecommercebackend.api.models.LoginRequest;
import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.exceptions.UserAlreadyExistsException;
import com.example.ecommercebackend.exceptions.UserNotVerifiedException;
import com.example.ecommercebackend.models.VerificationToken;
import com.example.ecommercebackend.models.repositories.VerificationTokenRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


/**
 * @author Mahyar Maleki
 */

@SpringBootTest
public class UserServiceTest {

    @RegisterExtension
    private static final GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("FirstName");
        registrationRequest.setLastName("LastName");
        registrationRequest.setPassword("MySecretPassword");

        registrationRequest.setUsername("UserA");
        registrationRequest.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(registrationRequest), "Username should already be in use.");

        registrationRequest.setEmail("UserA@junit.com");
        registrationRequest.setUsername("UserServiceTest$testRegisterUserA");
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(registrationRequest), "Email should already be in use.");


        registrationRequest.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(() -> userService.registerUser(registrationRequest), "User should register successfully.");

        Assertions.assertEquals(registrationRequest.getEmail(),
                greenMailExtension.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    @Transactional
    public void testVerifyUser() throws EmailFailureException {
        Assertions.assertFalse(userService.verifyUser("Bad Token"), "Token that is bad or does not exist should return false.");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("UserB");
        loginRequest.setPassword("PasswordB123");
        try {
            userService.loginUser(loginRequest);
            Assertions.fail("The user should not have email verified.");
        } catch (UserNotVerifiedException e) {
            List<VerificationToken> tokens = verificationTokenRepository.findByUser_IdOrderByIdDesc(2L);
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(userService.verifyUser(token), "Token should be valid.");
            Assertions.assertNotNull(loginRequest, "The user should now be verified.");
        }
    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, EmailFailureException {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername("UserA-NotExists");
        loginRequest.setPassword("PasswordA123");
        Assertions.assertNull(userService.loginUser(loginRequest), "The user should not exist.");

        loginRequest.setUsername("UserA");
        loginRequest.setPassword("PasswordA123-Incorrect");
        Assertions.assertNull(userService.loginUser(loginRequest), "The password should be incorrect.");

        loginRequest.setPassword("PasswordA123");
        Assertions.assertNotNull(userService.loginUser(loginRequest), "The user should login successfully.");

        loginRequest.setUsername("UserB");
        loginRequest.setPassword("PasswordB123");
        try {
            userService.loginUser(loginRequest);
            Assertions.fail("User should not have email verified.");
        } catch (UserNotVerifiedException ex) {
            Assertions.assertTrue(ex.isNewEmailSent(), "Email verification should be sent.");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try {
            userService.loginUser(loginRequest);
            Assertions.fail("User should not have email verified.");
        } catch (UserNotVerifiedException ex) {
            Assertions.assertFalse(ex.isNewEmailSent(), "Email verification should not be resent.");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }

    }

}
