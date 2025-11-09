package com.devops.payload.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidApiKeyExceptionTest {

    @Test
    void testInvalidApiKeyException_WithValidKey() {
        String key = "some-key";
        InvalidApiKeyException exception = new InvalidApiKeyException(key);

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid API Key"));
    }

    @Test
    void testInvalidApiKeyException_WithNullKey() {
        InvalidApiKeyException exception = new InvalidApiKeyException(null);

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid API Key"));
    }

    @Test
    void testInvalidApiKeyException_MessageDoesNotExposeKey() {
        String key = "sensitive-key-12345";
        InvalidApiKeyException exception = new InvalidApiKeyException(key);

        assertFalse(exception.getMessage().contains("sensitive-key-12345"));
    }

    @Test
    void testInvalidApiKeyException_IsRuntimeException() {
        InvalidApiKeyException exception = new InvalidApiKeyException("key");

        assertInstanceOf(RuntimeException.class, exception);
    }
}

