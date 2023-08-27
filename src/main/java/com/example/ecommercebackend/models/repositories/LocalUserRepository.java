package com.example.ecommercebackend.models.repositories;

import com.example.ecommercebackend.models.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Repository
public interface LocalUserRepository extends ListCrudRepository<LocalUser, Long> {
    Optional<LocalUser> findByUsernameIgnoreCase(String username);

    Optional<LocalUser> findByEmailIgnoreCase(String email);

}
