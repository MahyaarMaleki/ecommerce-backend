package com.example.ecommercebackend.models.repositories;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Repository
public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {
    void deleteByUser(LocalUser user);

    List<VerificationToken> findByUser_IdOrderByIdDesc(Long id);

    Optional<VerificationToken> findByToken(String token);
}
