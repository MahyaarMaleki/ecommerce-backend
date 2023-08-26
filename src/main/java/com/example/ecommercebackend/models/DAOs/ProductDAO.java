package com.example.ecommercebackend.models.DAOs;

import com.example.ecommercebackend.models.Product;
import org.springframework.data.repository.ListCrudRepository;

/**
 * @author Mahyar Maleki
 */


public interface ProductDAO extends ListCrudRepository<Product, Long> {
}
