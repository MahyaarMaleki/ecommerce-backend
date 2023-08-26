package com.example.ecommercebackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Mahyar Maleki
 */

@AllArgsConstructor
@Getter
public class UserNotVerifiedException extends Exception {
    private boolean isNewEmailSent;
}
