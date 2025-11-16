package com.orderservice.order.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderProductDto {
    private String workflow;
    private Map<String, Object> productDetails;
}

