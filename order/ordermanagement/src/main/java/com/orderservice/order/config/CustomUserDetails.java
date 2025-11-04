package com.orderservice.order.config;

import com.orderservice.order.util.JwtUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
public class CustomUserDetails implements UserDetails {

    private UUID userId;
    private  String username;
    private String role;

    public CustomUserDetails(String username, String role, UUID userId) {
        this.userId=userId;
        this.username = username;
        this.role=role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }
}
