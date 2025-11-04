package com.microservice.gateway.config;

import com.microservice.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class JwtAuthFiler implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
//        System.out.println(">>> Gateway incoming request path: " + path); // LOG 1: Incoming path

        // Skip auth for public endpoints
        if (path.contains("/api/auth/") || path.contains("/api/user/")) {
//            System.out.println(">>> Gateway skipping JWT validation for path: " + path); // LOG 2
            return chain.filter(exchange);
        }

        try {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//            System.out.println(">>> Gateway sees Authorization header: " + authHeader); // LOG 3

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                System.out.println(">>> Gateway blocking request: missing or malformed Authorization header"); // LOG 4
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            try {
                String token = authHeader.substring(7);
                jwtUtil.validateToken(token);
//                System.out.println(">>> Gateway JWT validation succeeded for token: " + token); // LOG 5

                // Forward the header downstream
                exchange = exchange.mutate()
                        .request(r -> r.header("Authorization", authHeader))
                        .build();
//                System.out.println(">>> Gateway forwarding request to downstream service"); // LOG 6
            } catch (Exception e) {
//                System.out.println(">>> Gateway JWT validation failed: " + e.getMessage()); // LOG 7
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        } catch (Exception e) {
            e.printStackTrace(); // LOG 8: Unexpected exception
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

