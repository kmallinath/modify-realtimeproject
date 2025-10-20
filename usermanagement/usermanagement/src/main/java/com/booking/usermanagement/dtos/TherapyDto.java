package com.booking.usermanagement.dtos;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TherapyDto {

    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Description cannot be null")
    private String description;
    @NotNull(message = "Price cannot be null")
    private Double price;
    private boolean active = true;

}
