package com.devops.payload.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devops.payload.exception.InvalidJwtException;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testValidateJwt_WithValidJwt_ShouldPass() {
        String validJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";
        assertDoesNotThrow(() -> jwtService.validateJwt(validJwt));
    }

    @Test
    void testValidateJwt_WithNullJwt_ShouldThrowException() {
        assertThrows(InvalidJwtException.class, () -> jwtService.validateJwt(null));
    }

    @Test
    void testValidateJwt_WithEmptyJwt_ShouldThrowException() {
        assertThrows(InvalidJwtException.class, () -> jwtService.validateJwt(""));
    }

    @Test
    void testValidateJwt_WithBlankJwt_ShouldThrowException() {
        assertThrows(InvalidJwtException.class, () -> jwtService.validateJwt("   "));
    }

    @Test
    void testValidateJwt_WithInvalidFormat_ShouldThrowException() {
        String invalidJwt = "invalid.jwt";
        assertThrows(InvalidJwtException.class, () -> jwtService.validateJwt(invalidJwt));
    }

    @Test
    void testValidateJwt_WithSinglePart_ShouldThrowException() {
        assertThrows(InvalidJwtException.class, () -> jwtService.validateJwt("singlepart"));
    }

    @Test
    void testValidateJwt_WithTwoParts_ShouldThrowException() {
        assertThrows(InvalidJwtException.class, () -> jwtService.validateJwt("part1.part2"));
    }

    @Test
    void testValidateJwt_WithMultipleParts_ShouldPass() {
        String multiPartJwt = "part1.part2.part3.part4";
        assertDoesNotThrow(() -> jwtService.validateJwt(multiPartJwt));
    }

    @Test
    void testValidateJwt_ExceptionMessage_ShouldBeConsistent() {
        try {
            jwtService.validateJwt(null);
            fail("Should have thrown InvalidJwtException");
        } catch (InvalidJwtException e) {
            assertEquals("Invalid or missing JWT token", e.getMessage());
        }
    }
}

