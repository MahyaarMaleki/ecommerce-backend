package com.example.ecommercebackend.models.DAOs;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * @author Mahyar Maleki
 */


public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {
    void deleteByUser(LocalUser user);
    Optional<VerificationToken> findByToken(String token);
}
