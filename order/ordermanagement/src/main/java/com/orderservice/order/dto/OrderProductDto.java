package com.orderservice.order.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderProductDto {
    private Map<String, Object> productDetails;
}

