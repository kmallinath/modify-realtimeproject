package com.orderservice.order.events;

import lombok.Data;

import java.io.Serializable;

@Data
public class EligibilityEvent  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eligibilityStatus;
    private String reason;
    private String orderId;
    private String eventType;
    private String createdBy;
}
