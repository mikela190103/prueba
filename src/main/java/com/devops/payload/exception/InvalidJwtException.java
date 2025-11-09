package com.devops.payload.exception;

public class InvalidJwtException extends RuntimeException {

    private final String token;

    public InvalidJwtException(String token) {
        super();
        this.token = token;
    }

    @Override
    public String getMessage() {
        return "Invalid or missing JWT token";
    }
}

