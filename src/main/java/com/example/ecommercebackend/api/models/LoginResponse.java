package com.example.ecommercebackend.api.models;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahyar Maleki
 */

@Getter
@Setter
public class LoginResponse extends BaseResponse {
    private String jwt;

    public LoginResponse(boolean isSuccessful, String responseMessage, String jwt) {
        super(isSuccessful, responseMessage);
        this.jwt = jwt;
    }
}
