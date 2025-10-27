package com.booking.usermanagement.config;

import com.booking.usermanagement.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.beans.Customizer;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomerUserDetailsService customerUserDetailsService;

    public AuthFilter(JwtUtil jwtUtil, CustomerUserDetailsService customerUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customerUserDetailsService = customerUserDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== AuthFilter: Request received ===");
        System.out.println("URI: " + request.getRequestURI());
        String path = request.getRequestURI();
        System.out.println("Method: " + request.getMethod());
        if (path.startsWith("/api/user/getbyemail") || path.startsWith("/api/user/register")) {
            System.out.println("Skipping JWT validation for: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.validateToken(token);
                String email = claims.get("email", String.class);
                System.out.println("Token valid for user: " + email);

                CustomerUserDetails userDetails = (CustomerUserDetails) customerUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.out.println("Error of Token validation: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            System.out.println("No Authorization header or invalid format" +authHeader);
            return;
        }

        // CRITICAL: Must call this to pass the request to the next filter/controller
        filterChain.doFilter(request, response);

        System.out.println("=== AuthFilter: Request completed ===");
    }
}