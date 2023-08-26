package com.example.ecommercebackend.services;

import com.example.ecommercebackend.exceptions.EmailFailureException;
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

    private JavaMailSender javaMailSender;

    private static final String VERIFICATION_EMAIL_SUBJECT = "Verify your email to complete your registration and activate your account.";
    private static final String VERIFICATION_EMAIL_TEXT = "Please follow the link bellow to verify your email and activate your account.";

    private SimpleMailMessage createMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);

        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage mailMessage = createMailMessage();
        mailMessage.setTo(verificationToken.getUser().getEmail());
        mailMessage.setSubject(VERIFICATION_EMAIL_SUBJECT);
        mailMessage.setText(VERIFICATION_EMAIL_TEXT + "\n" + url + "/auth/verify?token=" + verificationToken.getToken());

        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            throw new EmailFailureException();
        }
    }
}
