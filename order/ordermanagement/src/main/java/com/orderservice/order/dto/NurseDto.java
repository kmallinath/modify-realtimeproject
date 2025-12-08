package com.orderservice.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class NurseDto {

    private String name;
    private String email;
    private String phone;
    private OrganizationDto organizationDto;
    private List<TherapyDto> therapyNames;


}