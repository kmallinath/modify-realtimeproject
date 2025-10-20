package com.booking.usermanagement.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NurseOnboardRequest {
    private String name;
    private String email;
    private String phone;
    private UUID organizationId;
    private List<UUID> therapyIds;
}
