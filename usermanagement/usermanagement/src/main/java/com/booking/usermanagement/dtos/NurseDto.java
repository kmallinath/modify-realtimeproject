package com.booking.usermanagement.dtos;

import com.booking.usermanagement.entities.Therapy;
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
