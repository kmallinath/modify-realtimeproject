package com.booking.usermanagement.dtos;


import lombok.Data;

import java.util.UUID;

@Data
public class OrganizationDto {

    private UUID id;
    private String name;
    private String country;
    private String address;
    private String phone;
    private String email;

}
