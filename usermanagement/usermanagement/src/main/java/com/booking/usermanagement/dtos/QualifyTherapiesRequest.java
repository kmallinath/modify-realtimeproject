package com.booking.usermanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QualifyTherapiesRequest {
    @NotEmpty(message = "At least one therapy ID is required")
    private List<UUID> therapyIds;

    @NotBlank(message = "Qualifier is required")
    private String qualifiedBy;
}