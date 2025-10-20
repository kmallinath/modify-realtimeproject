package com.microservice.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // ORDER SERVICE - More specific route MUST come first
                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .uri("http://localhost:8081"))

                // USER MANAGEMENT - Less specific route comes second
                .route("user-management", r -> r
                        .path("/api/user/**")
                        .uri("http://localhost:8080"))

                .build();
    }
}