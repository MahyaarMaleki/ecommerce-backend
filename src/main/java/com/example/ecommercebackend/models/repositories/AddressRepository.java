package com.example.ecommercebackend.models.repositories;

import com.example.ecommercebackend.models.Address;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * @author Mahyar Maleki
 */


public interface AddressRepository extends ListCrudRepository<Address, Long> {
    List<Address> findByUser_Id(Long id);

}
