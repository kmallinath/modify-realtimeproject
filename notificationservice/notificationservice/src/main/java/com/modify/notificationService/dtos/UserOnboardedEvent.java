package com.modify.notificationService.dtos;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserOnboardedEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    private String email;
    private String name;
    private Integer veridicationCode;
}
