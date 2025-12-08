package com.orderservice.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderservice.order.config.CustomUserDetails;
import com.orderservice.order.dto.EligibilityDto;
import com.orderservice.order.dto.OrderDto;
import com.orderservice.order.dto.OrderProductDto;
import com.orderservice.order.dto.ProductReceiptDto;
import com.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    //swagger addition

    private final OrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal CustomUserDetails user, @RequestHeader("Authorization")String authorization) throws AccessDeniedException {




        System.out.println("Creating order for nurseId: " + user.getUsername());
        OrderDto orderDto=orderService.createOrder(user,authorization);
        return ResponseEntity.ok(orderDto);
    }


    // ---------------------------
    // 1️⃣ Save Eligibility
    // ---------------------------
    @PostMapping("/{orderId}/eligibility")
    public ResponseEntity<EligibilityDto> saveEligibility(
            @PathVariable("orderId") UUID orderId,
            @RequestBody EligibilityDto dto
    ) throws JsonProcessingException {
        EligibilityDto saved = orderService.saveEligibility(orderId, dto);
        return ResponseEntity.ok(saved);
    }

    // ---------------------------
    // 2️⃣ Save Order Product
    // ---------------------------
    @PostMapping("/{orderId}/product")
    public ResponseEntity<OrderProductDto> saveOrderProduct(
            @PathVariable("orderId") UUID orderId,
            @RequestBody OrderProductDto dto
    ) throws JsonProcessingException {
        OrderProductDto saved = orderService.saveOrderProduct(orderId, dto);
        return ResponseEntity.ok(saved);
    }

    // ---------------------------
    // 3️⃣ Save Product Receipt
    // ---------------------------
    @PostMapping("/{orderId}/receipt")
    public ResponseEntity<ProductReceiptDto> saveProductReceipt(
            @PathVariable("orderId") UUID orderId,
            @RequestBody ProductReceiptDto dto
    ) throws JsonProcessingException {
        ProductReceiptDto saved = orderService.saveProductReceipt(orderId, dto);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/debug-headers")
    public Map<String, String> debugHeaders(@RequestHeader Map<String, String> headers) {
        System.out.println(">>> All headers received by Order Service: " + headers);
        return headers;
    }

    @GetMapping("/getbynurse")
    public List<OrderDto> getOrdersByNurseId(@AuthenticationPrincipal CustomUserDetails user) {
        // Implementation to retrieve orders by nurseId
        return orderService.getOrdersByNurseId(user.getUsername());
    }

    @GetMapping("getbyid/{orderId}")
    public OrderDto getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }



}

