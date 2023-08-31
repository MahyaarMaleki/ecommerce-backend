package com.example.ecommercebackend.api.controllers.product;

import com.example.ecommercebackend.models.Product;
import com.example.ecommercebackend.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mahyar Maleki
 */

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
@Tag(name = "product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }
}
