package com.booking.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "organization_therapies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_org_therapy",
                        columnNames = {"organization_id", "therapy_id"}
                )
        }
)
@Data
public class OrganizationTherapy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapy_id", nullable = false)
    private Therapy therapy;

    @Column(nullable = false)
    private String qualifiedBy;

    @Column(nullable = false)
    private LocalDateTime qualifiedAt;

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE

    @PrePersist
    protected void onCreate() {
        if (qualifiedAt == null) {
            qualifiedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }

}