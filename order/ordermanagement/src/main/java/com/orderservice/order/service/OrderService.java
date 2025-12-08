package com.orderservice.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderservice.order.config.CustomUserDetails;
import com.orderservice.order.dto.*;
import com.orderservice.order.entity.Eligibility;
import com.orderservice.order.entity.OrderProduct;
import com.orderservice.order.entity.ProductReceipt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;


public interface OrderService {

    OrderDto createOrder(CustomUserDetails userDetails, String authorization) throws AccessDeniedException;
    public EligibilityDto saveEligibility(UUID orderId, EligibilityDto eligibilityDto) throws JsonProcessingException;
    public OrderProductDto saveOrderProduct(UUID orderId, OrderProductDto dto) throws JsonProcessingException;
    public ProductReceiptDto saveProductReceipt(UUID orderId,ProductReceiptDto productReceipt) throws JsonProcessingException;

    List<OrderDto> getOrdersByNurseId(String username);

    OrderDto getOrderById(UUID orderId);
}
