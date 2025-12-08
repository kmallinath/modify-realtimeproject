package com.modify.notificationService.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private String workflow;
    private String orderNumber;
    private String status;
    private String createdBy;
    private String approvedBy;
}
