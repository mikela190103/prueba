package com.devops.payload.exception;

public class InvalidApiKeyException extends RuntimeException {

    private final String providedKey;

    public InvalidApiKeyException(String providedKey) {
        super();
        this.providedKey = providedKey;
    }

    @Override
    public String getMessage() {
        return "Invalid API Key provided: " + (providedKey == null ? "null" : "***");
    }
}

