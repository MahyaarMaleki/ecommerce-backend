package com.example.ecommercebackend.api.models;

/**
 * @author Mahyar Maleki
 */

public class RegistrationResponse extends BaseResponse {
    public RegistrationResponse(boolean isSuccessful, String responseMessage) {
        super(isSuccessful, responseMessage);
    }
}
