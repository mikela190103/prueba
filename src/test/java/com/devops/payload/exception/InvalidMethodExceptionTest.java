package com.devops.payload.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidMethodExceptionTest {

    @Test
    void testInvalidMethodException_WithMethod() {
        String method = "GET";
        InvalidMethodException exception = new InvalidMethodException(method);

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("GET"));
        assertTrue(exception.getMessage().contains("Only POST is allowed"));
    }

    @Test
    void testInvalidMethodException_WithDifferentMethod() {
        String method = "PUT";
        InvalidMethodException exception = new InvalidMethodException(method);

        assertTrue(exception.getMessage().contains("PUT"));
    }

    @Test
    void testInvalidMethodException_IsRuntimeException() {
        InvalidMethodException exception = new InvalidMethodException("GET");

        assertInstanceOf(RuntimeException.class, exception);
    }
}

