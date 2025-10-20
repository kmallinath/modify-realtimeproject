package com.booking.usermanagement.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {



    public  ResourceNotFoundException(String user, String email, String email1) {
       super(String.format("%s not found with %s : %s", user, email, email1));
    }

    public  ResourceNotFoundException(String user, String s, UUID id) {

        super(String.format("%s not found with %s : %s", user, s, id));
    }

    public ResourceNotFoundException(String role, String id, long roleId) {
        super(String.format("%s not found with %s : %s", role, id, roleId));
    }

    public static class ExceptionHandler {
    }
}
