package com.example.ecommercebackend.services;

import com.example.ecommercebackend.exceptions.EmailFailureException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String url;

    private final JavaMailSender javaMailSender;

    private static final String VERIFICATION_EMAIL_SUBJECT = "Verify your email to complete your registration and activate your account.";
    private static final String VERIFICATION_EMAIL_TEXT = "Please follow the link bellow to verify your email and activate your account.";

    private static final String PASSWORD_RESET_EMAIL_SUBJECT = "Password Reset";
    private static final String PASSWORD_RESET_EMAIL_TEXT = "You requested a password reset on our website. " +
            "Please follow the link bellow to be able to reset your password.";


    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        String text = VERIFICATION_EMAIL_TEXT + "\n" + url + "/auth/verify?token=" + verificationToken.getToken();

        try {
            javaMailSender.send(createSimpleMailMessage(VERIFICATION_EMAIL_SUBJECT, text, verificationToken.getUser().getEmail()));
        } catch (MailException e) {
            throw new EmailFailureException();
        }
    }

    public void sendPasswordResetEmail(LocalUser user, String token) throws EmailFailureException {
        String text = PASSWORD_RESET_EMAIL_TEXT + "\n" + url + "/auth/reset?token=" + token;

        try {
            javaMailSender.send(createSimpleMailMessage(PASSWORD_RESET_EMAIL_SUBJECT, text, user.getEmail()));
        } catch (MailException e) {
            throw new EmailFailureException();
        }
    }

    private SimpleMailMessage createSimpleMailMessage(String subject, String text, String to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        simpleMailMessage.setFrom(fromAddress);
        simpleMailMessage.setTo(to);

        return simpleMailMessage;
    }
}
