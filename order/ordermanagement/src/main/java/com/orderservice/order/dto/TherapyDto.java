package com.orderservice.order.dto;

import lombok.Data;

@Data
public class TherapyDto {


    private String name;
    private String description;
    private Double price;
    private boolean active = true;

}
