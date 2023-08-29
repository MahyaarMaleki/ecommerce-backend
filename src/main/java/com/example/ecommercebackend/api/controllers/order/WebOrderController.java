package com.example.ecommercebackend.api.controllers.order;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.WebOrder;
import com.example.ecommercebackend.services.WebOrderService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class WebOrderController {
    private final WebOrderService webOrderService;

    @GetMapping
    public ResponseEntity<List<WebOrder>> getOrders(@AuthenticationPrincipal LocalUser user) {
        return ResponseEntity.ok(webOrderService.getOrders(user));
    }
}
