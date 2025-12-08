package com.orderservice.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "workflow_schemas")
@Data
public class WorkflowSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "workflow_name", nullable = false, unique = true)
    private String workflowName; // "WORKFLOW_US_V1", "WORKFLOW_IN_V1"

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String eligibilitySchema; // JSON field definitions

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String orderProductSchema; // JSON field definitions

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String receiptSchema; // JSON field definitions

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}