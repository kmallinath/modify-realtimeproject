package com.orderservice.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
@Entity
@Table(name = "current_country_workflow_mapping")
public class CurrentCountryWorkflowMapping {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "country_code", nullable = false, unique = true, length = 10)
    private String countryCode;

    @Column(name = "workflow_name", nullable = false)
    private String workflowName;

    // Getters and setters
}
