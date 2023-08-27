package com.example.ecommercebackend.models.repositories;

import com.example.ecommercebackend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mahyar Maleki
 */

@Repository
public interface ProductRepository extends ListCrudRepository<Product, Long> {
}
