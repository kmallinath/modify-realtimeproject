package com.modify.notificationService.events;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eventType;
    private Object payload;
}
