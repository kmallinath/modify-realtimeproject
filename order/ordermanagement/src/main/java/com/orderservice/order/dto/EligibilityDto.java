package com.orderservice.order.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EligibilityDto {
    private Map<String, Object> responses;
}
