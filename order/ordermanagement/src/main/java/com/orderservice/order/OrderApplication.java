package com.orderservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {




		ApplicationContext context = SpringApplication.run(OrderApplication.class, args);

	}

}
