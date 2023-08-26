package com.example.ecommercebackend.models.repositories;

import com.example.ecommercebackend.models.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

/**
 * @author Mahyar Maleki
 */


public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {
}
