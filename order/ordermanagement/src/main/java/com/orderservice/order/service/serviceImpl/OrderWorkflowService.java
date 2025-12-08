package com.orderservice.order.service.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.order.config.CustomUserDetails;
import com.orderservice.order.config.KafkaConfig;
import com.orderservice.order.dto.*;
import com.orderservice.order.entity.*;
import com.orderservice.order.events.EligibilityEvent;
import com.orderservice.order.events.EmailEvent;
import com.orderservice.order.events.OrderOnboardEvent;
import com.orderservice.order.exception.ResourceAlreadyExists;
import com.orderservice.order.exception.ResourceNotFoundException;
import com.orderservice.order.repository.*;
import com.orderservice.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Service
public class OrderWorkflowService implements OrderService {




    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final EligibilityRepository eligibilityRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductReceiptRepository receiptRepository;
    private final OrderRepository orderRepository;
    private final WorkflowCountryRepository workflowCountryRepository;

    @Autowired
    public OrderWorkflowService(ObjectMapper objectMapper, EligibilityRepository eligibilityRepository, OrderProductRepository orderProductRepository, ProductReceiptRepository receiptRepository, OrderRepository orderRepository, WorkflowCountryRepository workflowCountryRepository) {
        this.objectMapper = objectMapper;
        this.eligibilityRepository = eligibilityRepository;
        this.orderProductRepository = orderProductRepository;
        this.receiptRepository = receiptRepository;
        this.orderRepository = orderRepository;
        this.workflowCountryRepository = workflowCountryRepository;
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
                    new HttpEntity<>(headers),
                    NurseDto.class
            ).getBody();

            if (userDto == null) {
                throw new ResourceNotFoundException("Nurse", userId);
            }

        } catch (Exception e) {
            // Log and rethrow as AccessDenied if it’s a 403/401
            throw new AccessDeniedException("Failed to fetch nurse details: " + e.getMessage());
        }
        String country=userDto.getOrganizationDto().getCountry();
        String workflow=workflowCountryRepository.findByCountryCode(country)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow for country"+country))
                .getWorkflowName();
        Order order = new Order();
        order.setCreatedBy(userDto.getEmail());
        order.setStatus("ELIGIBILITY");
        order.setWorkflow(workflow);

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
        dto.setWorkflow(order.getWorkflow());
        dto.setOrderNumber(order.getOrderNumber());  // Don't forget this!

        try {


            OrderOnboardEvent onboardEvent = new OrderOnboardEvent();
            onboardEvent.setEventType("ORDER_CREATED");
            onboardEvent.setReason("New order created");
            onboardEvent.setOrderId(dto.getId());
            onboardEvent.setStatus("passed");
            onboardEvent.setCreatedBy(dto.getCreatedBy());
            EmailEvent emailEvent=new EmailEvent();
            emailEvent.setEventType("ORDER_CREATED");
            emailEvent.setPayload(onboardEvent);
            kafkaTemplate.send("order-topic", emailEvent);
            log.info("Order created with ID: {}", dto.getId());
        }
        catch (Exception e){
            log.error("Failed to send Kafka event for order ID: {}", dto.getId(), e);
        }
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
        if (!"ELIGIBILITY".equals(order.getStatus())) {
            throw new IllegalStateException("Invalid workflow state. Expected ELIGIBILITY but found: " + order.getStatus());
        }

        Eligibility eligibility = new Eligibility();
        eligibility.setOrder(order);
        eligibility.setWorkflow(order.getWorkflow());
        eligibility.setResponses(objectMapper.writeValueAsString(dto.getResponses()));
        order.setEligibility(eligibility);
        order.setStatus("ORDER_PRODUCT");
        orderRepository.save(order);

        dto.setWorkflow(order.getWorkflow());

        EligibilityEvent eligibilityEvent = new EligibilityEvent();
        eligibilityEvent.setOrderId(order.getId().toString());
        eligibilityEvent.setEligibilityStatus("COMPLETED");
        eligibilityEvent.setReason("Eligibility process completed successfully.");
        eligibilityEvent.setCreatedBy(order.getCreatedBy());
        EmailEvent emailEvent=new EmailEvent();
        emailEvent.setEventType("ELIGIBILITY_COMPLETED");
        emailEvent.setPayload(eligibilityEvent);
        kafkaTemplate.send("order-topic",emailEvent);
        return dto;

    }

    public OrderProductDto saveOrderProduct(UUID orderId, OrderProductDto dto) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));

        if (!"ORDER_PRODUCT".equals(order.getStatus())) {
            throw new IllegalStateException("Invalid workflow state. Expected ORDER_PRODUCT but found: " + order.getStatus());
        }

        OrderProduct product = new OrderProduct();
        product.setOrder(order);
        product.setWorkflow(order.getWorkflow());
        product.setProductDetails(objectMapper.writeValueAsString(dto.getProductDetails()));
        order.setOrderProduct(product);
        order.setStatus("PRODUCT_RECEIPT");
        orderRepository.save(order);
        dto.setWorkflow(order.getWorkflow());// save the order to persist product
        return dto;



    }

    public ProductReceiptDto saveProductReceipt(UUID orderId, ProductReceiptDto dto) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));

        if(!"PRODUCT_RECEIPT".equals(order.getStatus())) {
            throw new IllegalStateException("Invalid workflow state. Expected PRODUCT_RECEIPT but found: " + order.getStatus());
        }

        ProductReceipt receipt = new ProductReceipt();
        receipt.setOrder(order);
        receipt.setWorkflow(order.getWorkflow());
        receipt.setReceiptData(objectMapper.writeValueAsString(dto.getReceiptData()));
        order.setReceipt(receipt);
        order.setStatus("COMPLETED");
        orderRepository.save(order);
        dto.setWorkflow(order.getWorkflow());// save the order to persist receipt
        return dto;


    }

    @Override
    public List<OrderDto> getOrdersByNurseId(String username) {
        // Implementation for fetching orders by nurse ID
        List<Order> savedOrders=orderRepository.findByCreatedBy(username);
        return savedOrders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setStatus(order.getStatus());
            dto.setCreatedBy(order.getCreatedBy());
            dto.setWorkflow(order.getWorkflow());
            dto.setOrderNumber(order.getOrderNumber());
            return dto;
        }).toList();
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        Order orderDto=orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));
        OrderDto orderDto1=new OrderDto();
        orderDto1.setId(orderDto.getId());
        orderDto1.setStatus(orderDto.getStatus());
        orderDto1.setCreatedBy(orderDto.getCreatedBy());
        orderDto1.setApprovedBy(orderDto.getApprovedBy());
        orderDto1.setWorkflow(orderDto.getWorkflow());
        orderDto1.setOrderNumber(orderDto.getOrderNumber());
        return orderDto1;
    }


}
