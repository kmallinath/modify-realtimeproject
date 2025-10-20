package com.orderservice.order.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {



    public  ResourceNotFoundException(String resource, UUID id) {
       super(String.format("%s not found with id  %s", resource, id));
    }


    public static class ExceptionHandler {
    }
}
