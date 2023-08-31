package com.example.ecommercebackend.api.models;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "Operation result")
    private boolean isSuccessful;
    @Schema(title = "Response message", example = "SAMPLE_RESPONSE_MESSAGE")
    private String responseMessage;
}
