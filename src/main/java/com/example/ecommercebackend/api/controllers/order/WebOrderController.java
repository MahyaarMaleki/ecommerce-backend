package com.example.ecommercebackend.api.controllers.order;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.WebOrder;
import com.example.ecommercebackend.services.WebOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mahyar Maleki
 */

@RestController
@RequestMapping(path = "/order", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Tag(name = "order")
public class WebOrderController {
    private final WebOrderService webOrderService;

    @Operation(summary = "Get all the user's orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "You are either logged out or trying to get someone else's orders.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Retrieved successfully.")
    })
    @GetMapping
    public ResponseEntity<List<WebOrder>> getOrders(@AuthenticationPrincipal LocalUser user) {
        return ResponseEntity.ok(webOrderService.getOrders(user));
    }
}
