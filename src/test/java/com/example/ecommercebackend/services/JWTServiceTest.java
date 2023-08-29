package com.example.ecommercebackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.LocalUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Mahyar Maleki
 */

@SpringBootTest
@AutoConfigureMockMvc
public class JWTServiceTest {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

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

    @Test
    public void testLoginJWTNotGeneratedByUs() {
        String token = JWT.create().withClaim("USERNAME", "UserA").sign(Algorithm.HMAC256("NotTheRealSecretKey"));
        Assertions.assertThrows(SignatureVerificationException.class, () -> jwtService.getUsername(token));
    }

    @Test
    public void testLoginJWTCorrectlySignedWithNoIssuer() {
        String token = JWT.create().withClaim("USERNAME", "UserA").sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class, () -> jwtService.getUsername(token));
    }

    @Test
    public void testPasswordResetToken() {
        LocalUser user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generatePasswordResetJWT(user);
        Assertions.assertEquals(user.getEmail(), jwtService.getResetPasswordEmail(token), "Email should match inside JWT.");
    }

    @Test
    public void testPasswordResetJWTNotGeneratedByUs() {
        String token = JWT.create().withClaim("RESET_PASSWORD_EMAIL", "UserA@junit.com").sign(Algorithm.HMAC256("NotTheRealSecretKey"));
        Assertions.assertThrows(SignatureVerificationException.class, () -> jwtService.getResetPasswordEmail(token));
    }

    @Test
    public void testPasswordResetJWTCorrectlySignedWithNoIssuer() {
        String token = JWT.create().withClaim("RESET_PASSWORD_EMAIL", "UserA@junit.com").sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class, () -> jwtService.getResetPasswordEmail(token));
    }
}
