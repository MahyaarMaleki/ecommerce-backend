package com.example.ecommercebackend.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.ecommercebackend.models.LocalUser;
import com.example.ecommercebackend.models.repositories.LocalUserRepository;
import com.example.ecommercebackend.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mahyar Maleki
 */

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter implements ChannelInterceptor {

    private final LocalUserRepository localUserRepository;

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken token = checkToken(tokenHeader);
        if(token != null) {
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken checkToken(String token) {
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtService.getUsername(token);
                Optional<LocalUser> optionalUser = localUserRepository.findByUsernameIgnoreCase(username);
                if(optionalUser.isPresent()) {
                    LocalUser user = optionalUser.get();
                    if(user.getIsEmailVerified()) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return authentication;
                    }
                }
            } catch (JWTDecodeException e) {

            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return null;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        Map nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");
        if(message.getHeaders().get("simpMessageType").equals(SimpMessageType.CONNECT)) {
            if (nativeHeaders != null) {
                List authTokenList = (List) nativeHeaders.get("Authorization");
                if (authTokenList != null) {
                    String tokenHeader = (String) authTokenList.get(0);
                    checkToken(tokenHeader);
                }
            }
        }
        return message;
    }
}
