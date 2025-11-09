package com.devops.payload.exception;

public class InvalidMethodException extends RuntimeException {

    private final String method;

    public InvalidMethodException(String method) {
        super();
        this.method = method;
    }

    @Override
    public String getMessage() {
        return "Invalid HTTP method: " + method + ". Only POST is allowed.";
    }
}

