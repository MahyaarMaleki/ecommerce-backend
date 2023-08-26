package com.example.ecommercebackend.models.DAOs;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * @author Mahyar Maleki
 */


public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long> {
    List<WebOrder> findByUser(LocalUser user);
}
