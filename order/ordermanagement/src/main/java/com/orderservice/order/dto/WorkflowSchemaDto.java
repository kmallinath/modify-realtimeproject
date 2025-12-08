package com.orderservice.order.dto;


import lombok.Data;

@Data
public class WorkflowSchemaDto {
    private String workflowName;
    private String eligibilitySchema; // JSON string
    private String orderProductSchema; // JSON string
    private String receiptSchema; // JSON string
}