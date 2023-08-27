package com.example.ecommercebackend.services;

import com.example.ecommercebackend.models.Product;
import com.example.ecommercebackend.models.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
