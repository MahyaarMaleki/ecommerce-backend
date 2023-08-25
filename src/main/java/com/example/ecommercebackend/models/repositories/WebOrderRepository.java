package com.example.ecommercebackend.models.repositories;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.WebOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Mahyar Maleki
 */

@Repository
public interface WebOrderRepository extends ListCrudRepository<WebOrder, Long> {
    List<WebOrder> findByLocalUser(LocalUser localUser);
}
