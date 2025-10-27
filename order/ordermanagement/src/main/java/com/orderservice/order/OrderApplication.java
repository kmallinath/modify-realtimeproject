package com.orderservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {




		ApplicationContext context = SpringApplication.run(OrderApplication.class, args);

		System.out.println("=== Loaded Beans Related to Security ===");
		Arrays.stream(context.getBeanDefinitionNames())
				.filter(b -> b.toLowerCase().contains("security") || b.toLowerCase().contains("auth"))
				.sorted()
				.forEach(System.out::println);
	}

}
