package com.example.ecommercebackend.api.models;

/**
 * @author Mahyar Maleki
 */

public class VerificationResponse extends BaseResponse {
    public VerificationResponse(boolean isSuccessful, String responseMessage) {
        super(isSuccessful, responseMessage);
    }
}
