package com.example.ecommercebackend.api.controllers.auth;

import com.example.ecommercebackend.api.models.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mahyar Maleki
 */

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @RegisterExtension
    private static final GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Test
    public void testRegister() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RegistrationRequest registrationRequest = new RegistrationRequest();

        // not being null test

        registrationRequest.setUsername(null);
        registrationRequest.setEmail("AuthenticationControllerTest$testRegister@junit.com");
        registrationRequest.setPassword("Password!123");
        registrationRequest.setFirstName("FirstName");
        registrationRequest.setLastName("LastName");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setUsername("AuthenticationControllerTest$testRegister");
        registrationRequest.setEmail(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setEmail("AuthenticationControllerTest$testRegister@junit.com");
        registrationRequest.setPassword(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("Password!123");
        registrationRequest.setFirstName(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setFirstName("FirstName");
        registrationRequest.setLastName(null);
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setLastName("LastName");

        // not being empty test

        registrationRequest.setUsername("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setUsername("AuthenticationControllerTest$testRegister");
        registrationRequest.setEmail("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setEmail("AuthenticationControllerTest$testRegister@junit.com");
        registrationRequest.setPassword("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("Password!123");
        registrationRequest.setFirstName("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setFirstName("FirstName");
        registrationRequest.setLastName("");
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setLastName("LastName");

        // username length test

        registrationRequest.setUsername("me"); // min length is 3
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setUsername("AuthenticationControllerTest$testRegisterAuthenticationControllerTest$testRegister" +
                "AuthenticationControllerTest$testRegisterAuthenticationControllerTest$testRegister" +
                "AuthenticationControllerTest$testRegisterAuthenticationControllerTest$testRegister" +
                "AuthenticationControllerTest$testRegisterAuthenticationControllerTest$testRegister"); // max length is 255
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setUsername("AuthenticationControllerTest$testRegister");

        // password pattern test

        registrationRequest.setPassword("Aa1!2"); // min length is 6

        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("AuthenticationControllerTest$testRegister123"); // max length is 32
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("password!123"); // should contain at least 1 uppercase character
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("PASSWORD!123"); // should contain at least 1 lowercase character
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("Password123"); // should contain at least 1 special character
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        registrationRequest.setPassword("Password!123");

        // successful register test

        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.OK.value()));

    }

    // TODO: Test email validity
}
