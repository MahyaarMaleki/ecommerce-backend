package com.example.ecommercebackend.services;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.WebOrder;
import com.example.ecommercebackend.models.DAOs.WebOrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mahyar Maleki
 */

@Service
@RequiredArgsConstructor
public class WebOrderService {
    private final WebOrderDAO webOrderDAO;

    public List<WebOrder> getOrders(LocalUser user) {
        return webOrderDAO.findByUser(user);
    }
}
