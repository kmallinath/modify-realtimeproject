package com.booking.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;


//  a full user entity with id, name, email, password, role, createdAt, updatedAt, created by, updatedBy
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;
    @CreatedDate
    @Column(updatable = false,nullable = false)
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private String createdBy="system";
    private String updatedBy="system";




}
