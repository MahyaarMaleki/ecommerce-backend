package com.example.ecommercebackend.models.DAOs;

import com.example.ecommercebackend.models.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * @author Mahyar Maleki
 */


public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {
    Optional<LocalUser> findByUsernameIgnoreCase(String username);

    Optional<LocalUser> findByEmailIgnoreCase(String email);

}
