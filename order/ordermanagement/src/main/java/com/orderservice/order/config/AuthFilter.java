package com.orderservice.order.config;

import com.orderservice.order.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class AuthFilter extends OncePerRequestFilter {


    private JwtUtil jwtUtil;



    public AuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("Auth header: " + request.getHeader("Authorization"));

        try{
            String authHeader = request.getHeader("Authorization");
            if(authHeader==null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }
            String token = authHeader.substring(7);

            Claims claims = jwtUtil.validateToken(token);
            String username = claims.get("email", String.class);
            System.out.println("Authenticated user: " + username);
            Date issuedTime = claims.getIssuedAt();
            Date expirationTime = claims.getExpiration();
            String role = claims.get("role", String.class);
            if(expirationTime.before(new Date())) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, " Token has expired");
                return;
            };
            CustomUserDetails userDetails = new CustomUserDetails(username,role);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            System.out.println("Request processed at: " + LocalDateTime.now());
        }
        catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return; // ADD THIS LINE
        }
    }
}
