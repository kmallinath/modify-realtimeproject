
package com.orderservice.order.controller;

import com.orderservice.order.dto.WorkflowSchemaDto;

import com.orderservice.order.service.serviceImpl.WorkflowSchemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders/workflow-schemas")
public class WorkflowSchemaController {

    private final WorkflowSchemaService schemaService;

    public WorkflowSchemaController(WorkflowSchemaService schemaService) {
        this.schemaService = schemaService;
    }

    // Frontend calls this to get form structure for an order
    @GetMapping("/getbyorder/{orderId}")
    public ResponseEntity<WorkflowSchemaDto> getSchemaForOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(schemaService.getSchemaForOrder(orderId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrUpdateSchema(@RequestBody WorkflowSchemaDto schemaDto) {
        // Convert DTO to entity
        var schema = new com.orderservice.order.entity.WorkflowSchema();
        schema.setWorkflowName(schemaDto.getWorkflowName());
        schema.setEligibilitySchema(schemaDto.getEligibilitySchema());
        schema.setOrderProductSchema(schemaDto.getOrderProductSchema());
        schema.setReceiptSchema(schemaDto.getReceiptSchema());

        schemaService.saveSchema(schema);
        return ResponseEntity.ok("Workflow schema saved successfully");
    }
}