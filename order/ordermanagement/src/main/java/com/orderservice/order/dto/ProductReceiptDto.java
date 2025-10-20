package com.orderservice.order.dto;


import lombok.Data;

import java.util.Map;

@Data
public class ProductReceiptDto {
    private Map<String, Object> receiptData;
}
