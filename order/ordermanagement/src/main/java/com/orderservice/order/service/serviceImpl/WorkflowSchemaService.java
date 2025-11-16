package com.orderservice.order.service.serviceImpl;


import com.orderservice.order.dto.WorkflowSchemaDto;
import com.orderservice.order.entity.Order;
import com.orderservice.order.entity.WorkflowSchema;
import com.orderservice.order.exception.ResourceNotFoundException;
import com.orderservice.order.repository.OrderRepository;
import com.orderservice.order.repository.WorkflowSchemaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkflowSchemaService {

    private final WorkflowSchemaRepository schemaRepository;
    private final OrderRepository orderRepository;

    public WorkflowSchemaService(WorkflowSchemaRepository schemaRepository, OrderRepository orderRepository) {
        this.schemaRepository = schemaRepository;
        this.orderRepository = orderRepository;
    }

    // Get schema for a specific order
    public WorkflowSchemaDto getSchemaForOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        WorkflowSchema schema = schemaRepository.findByWorkflowName(order.getWorkflow())
                .orElseThrow(() -> new ResourceNotFoundException("Workflow Schema", order.getWorkflow()));

        return convertToDto(schema);
    }

    // Create/Update schema (Admin function)
    public WorkflowSchema saveSchema(WorkflowSchema schema) {
        return schemaRepository.save(schema);
    }

    private WorkflowSchemaDto convertToDto(WorkflowSchema schema) {
        WorkflowSchemaDto dto = new WorkflowSchemaDto();
        dto.setWorkflowName(schema.getWorkflowName());
        dto.setEligibilitySchema(schema.getEligibilitySchema());
        dto.setOrderProductSchema(schema.getOrderProductSchema());
        dto.setReceiptSchema(schema.getReceiptSchema());
        return dto;
    }
}