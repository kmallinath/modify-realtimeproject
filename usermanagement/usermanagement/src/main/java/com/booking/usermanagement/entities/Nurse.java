package com.booking.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "nurses")
@Data
public class Nurse {
    @Id
    private UUID id;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "nurse_therapies",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "therapy_id")
    )
    private List<Therapy> therapies;
}
