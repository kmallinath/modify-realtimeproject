package com.microservice.gateway.dtos;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class UserDto   {

//    @Serial
//    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Long roleId;
    private String roleName;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;


}
