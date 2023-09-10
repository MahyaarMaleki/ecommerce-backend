package com.example.ecommercebackend.annotation.constraint;

import com.example.ecommercebackend.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;


import java.util.List;


import static org.passay.EnglishCharacterData.*;

/**
 * @author Mahyar Maleki
 */


public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<Rule> rules = List.of(
                new LengthRule(8, 32),
                new CharacterRule(UpperCase, 1),
                new CharacterRule(LowerCase, 1),
                new CharacterRule(Digit, 1),
                new CharacterRule(Special, 1),
                new WhitespaceRule()
        );
        PasswordValidator validator = new PasswordValidator(rules);

        RuleResult result = validator.validate(new PasswordData(password));
        if(result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(",", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}
