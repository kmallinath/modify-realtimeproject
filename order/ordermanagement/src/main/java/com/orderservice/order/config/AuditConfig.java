package com.orderservice.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableJpaAuditing

public class AuditConfig {



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
