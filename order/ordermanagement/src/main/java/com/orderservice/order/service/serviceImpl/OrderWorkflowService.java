package com.orderservice.order.service.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.order.config.CustomUserDetails;
import com.orderservice.order.dto.*;
import com.orderservice.order.entity.Eligibility;
import com.orderservice.order.entity.Order;
import com.orderservice.order.entity.OrderProduct;
import com.orderservice.order.entity.ProductReceipt;
import com.orderservice.order.exception.ResourceAlreadyExists;
import com.orderservice.order.exception.ResourceNotFoundException;
import com.orderservice.order.repository.EligibilityRepository;
import com.orderservice.order.repository.OrderProductRepository;
import com.orderservice.order.repository.OrderRepository;
import com.orderservice.order.repository.ProductReceiptRepository;
import com.orderservice.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.file.AccessDeniedException;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
public class OrderWorkflowService implements OrderService {



    @Autowired
    private RestTemplate restTemplate;


    private final ObjectMapper objectMapper;
    private final EligibilityRepository eligibilityRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductReceiptRepository receiptRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderWorkflowService(ObjectMapper objectMapper, EligibilityRepository eligibilityRepository, OrderProductRepository orderProductRepository, ProductReceiptRepository receiptRepository, OrderRepository orderRepository) {
        this.objectMapper = objectMapper;
        this.eligibilityRepository = eligibilityRepository;
        this.orderProductRepository = orderProductRepository;
        this.receiptRepository = receiptRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderDto createOrder(@AuthenticationPrincipal CustomUserDetails userDetais, String authorization) throws AccessDeniedException {

        UUID userId=userDetais.getUserId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        NurseDto userDto;
        try {
            // ✅ Correctly capture response body
            userDto = restTemplate.exchange(
                    "http://localhost:8080/api/user/nurses/get/id/" + userId,
                    HttpMethod.GET,
                    new org.springframework.http.HttpEntity<>(headers),
                    NurseDto.class
            ).getBody();

            if (userDto == null) {
                throw new ResourceNotFoundException("Nurse", userId);
            }

        } catch (Exception e) {
            // Log and rethrow as AccessDenied if it’s a 403/401
            throw new AccessDeniedException("Failed to fetch nurse details: " + e.getMessage());
        }

        Order order = new Order();
        order.setCreatedBy(userDto.getEmail());
        order.setStatus("ELIGIBILITY");

        // First save to get the UUID
        order = orderRepository.save(order);

        // Generate order number using the UUID
        String derived = generateSequentialFromUUID(order.getId());
        order.setOrderNumber("PAT" + derived);

        // No need for explicit second save - JPA dirty checking will handle it
        // But you can add it for clarity: order = orderRepository.save(order);
        order.setOrganization(userDto.getOrganizationDto().getId());
        order=orderRepository.save(order);

        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setCreatedBy(order.getCreatedBy());
        dto.setOrderNumber(order.getOrderNumber());  // Don't forget this!
        return dto;
    }

    private String generateSequentialFromUUID(UUID uuid) {
        long uuidAsLong = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        int fourDigit = (int) (Math.abs(uuidAsLong) % 10000);
        return String.format("%04d", fourDigit);
    }

    public EligibilityDto saveEligibility(UUID orderId, EligibilityDto dto) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        Eligibility eligibility = new Eligibility();
        eligibility.setOrder(order);
        eligibility.setResponses(objectMapper.writeValueAsString(dto.getResponses()));
        order.setEligibility(eligibility);
        orderRepository.save(order);
        return dto;

    }

    public OrderProductDto saveOrderProduct(UUID orderId, OrderProductDto dto) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));

        OrderProduct product = new OrderProduct();
        product.setOrder(order);
        product.setProductDetails(objectMapper.writeValueAsString(dto.getProductDetails()));
        order.setOrderProduct(product);
        orderRepository.save(order); // save the order to persist product
        return dto;



    }

    public ProductReceiptDto saveProductReceipt(UUID orderId, ProductReceiptDto dto) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));

        ProductReceipt receipt = new ProductReceipt();
        receipt.setOrder(order);
        receipt.setReceiptData(objectMapper.writeValueAsString(dto.getReceiptData()));
        order.setReceipt(receipt);
        orderRepository.save(order);
        return dto;


    }
}
