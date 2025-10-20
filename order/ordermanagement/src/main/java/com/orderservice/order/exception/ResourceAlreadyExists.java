package com.orderservice.order.exception;

public class ResourceAlreadyExists extends RuntimeException {
    public ResourceAlreadyExists(String message) {
        super(message);
    }

    public ResourceAlreadyExists(String resource, String field, String value) {
        super(String.format("%s already exists with %s : %s", resource, field, value));
    }
}
