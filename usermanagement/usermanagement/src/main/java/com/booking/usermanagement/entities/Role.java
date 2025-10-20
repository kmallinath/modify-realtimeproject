package com.booking.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;
}
