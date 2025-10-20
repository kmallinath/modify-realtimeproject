package com.booking.usermanagement.dtos;

import com.booking.usermanagement.entities.User;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {

    private String name;
    private String description;
    private boolean active;


}
