package com.devops.payload.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidJwtExceptionTest {

    @Test
    void testInvalidJwtException_WithValidToken() {
        String token = "some-token";
        InvalidJwtException exception = new InvalidJwtException(token);

        assertNotNull(exception.getMessage());
        assertEquals("Invalid or missing JWT token", exception.getMessage());
    }

    @Test
    void testInvalidJwtException_WithNullToken() {
        InvalidJwtException exception = new InvalidJwtException(null);

        assertNotNull(exception.getMessage());
        assertEquals("Invalid or missing JWT token", exception.getMessage());
    }

    @Test
    void testInvalidJwtException_IsRuntimeException() {
        InvalidJwtException exception = new InvalidJwtException("token");

        assertInstanceOf(RuntimeException.class, exception);
    }
}

