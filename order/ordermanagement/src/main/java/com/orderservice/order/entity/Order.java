package com.orderservice.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "order_number", nullable = true, unique = true)
    private String orderNumber;

    @Column(nullable = true)
    private String organization;

    @Column(nullable = false)
    private String status; // ELIGIBILITY, PRODUCT_SUBMITTED, APPROVED, RECEIPT_SUBMITTED

    @Column(nullable = false)
    private String createdBy; // nurse email (from User Service)

    @Column(nullable = true)
    private String approvedBy; // sponsorId

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Eligibility eligibility;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderProduct orderProduct;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductReceipt receipt;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Order() {

    }
}
