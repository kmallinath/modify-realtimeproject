package com.modify.notificationService.events;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class OrderOnboardEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    private String status;
    private String reason;
    private UUID orderId;
    private  String eventType;
    private String createdBy;
}
