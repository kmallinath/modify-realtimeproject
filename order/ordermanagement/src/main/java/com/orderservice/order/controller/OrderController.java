package com.orderservice.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderservice.order.dto.EligibilityDto;
import com.orderservice.order.dto.OrderDto;
import com.orderservice.order.dto.OrderProductDto;
import com.orderservice.order.dto.ProductReceiptDto;
import com.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestParam UUID nurseId) {

        return ResponseEntity.ok(orderService.createOrder(nurseId));
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
}

