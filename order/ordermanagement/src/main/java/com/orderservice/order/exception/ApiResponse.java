package com.orderservice.order.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse {

    private String message;
    private boolean success;
    private HttpStatus status;

    public ApiResponse(String message, boolean b, HttpStatus httpStatus) {
        this.message = message;
        this.success = b;
        this.status = httpStatus;
    }
}
