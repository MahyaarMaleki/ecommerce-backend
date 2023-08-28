package com.example.ecommercebackend.services;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.LocalUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Mahyar Maleki
 */

@SpringBootTest
@AutoConfigureMockMvc
public class JWTServiceTest {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserRepository userRepository;

    @Test
    public void testVerificationTokenNotUsableForLogin() {
        LocalUser user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateVerificationJWT(user);
        Assertions.assertNull(jwtService.getUsername(token), "Verification token should not contain username.");
    }

    @Test
    public void testAuthTokenReturnsUsername() {
        LocalUser user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateJWT(user);
        Assertions.assertEquals(user.getUsername(), jwtService.getUsername(token), "Token for auth should contain user's username.");
    }
}
