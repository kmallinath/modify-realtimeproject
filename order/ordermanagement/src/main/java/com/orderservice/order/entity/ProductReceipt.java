package com.orderservice.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
public class ProductReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String workflow;

    // Flexible product details (productName, quantity, dosage, etc.)
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)// PostgreSQL JSONB column
    private String receiptData;
}
