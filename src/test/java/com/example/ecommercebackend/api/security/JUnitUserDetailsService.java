package com.example.ecommercebackend.api.security;

import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Service
@Primary
public class JUnitUserDetailsService implements UserDetailsService {

    @Autowired
    private LocalUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LocalUser> optionalUser = userRepository.findByUsernameIgnoreCase(username);
        return optionalUser.orElse(null);
    }
}
