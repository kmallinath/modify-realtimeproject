package com.orderservice.order.repository;

import com.orderservice.order.entity.WorkflowSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowSchemaRepository extends JpaRepository<WorkflowSchema, UUID> {
    Optional<WorkflowSchema> findByWorkflowName(String workflowName);
}