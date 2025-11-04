package com.orderservice.order.dto;

import jakarta.persistence.GeneratedValue;
import lombok.Data;
import lombok.Generated;

import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private String orderNumber;
    private String status;
    private String createdBy;
    private String approvedBy;
}
