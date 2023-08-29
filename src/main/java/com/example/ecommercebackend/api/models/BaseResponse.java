package com.example.ecommercebackend.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahyar Maleki
 */

@AllArgsConstructor
@Getter
@Setter
public class BaseResponse {

    private boolean isSuccessful;

    private String responseMessage;
}
