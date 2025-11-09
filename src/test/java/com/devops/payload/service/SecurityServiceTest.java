package com.devops.payload.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devops.payload.exception.InvalidApiKeyException;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        securityService = new SecurityService();
    }

    @Test
    void testValidateApiKey_WithValidKey_ShouldPass() {
        String validApiKey = "2f5ae96c-b558-4c7b-a590-a501ae1c3f6c";
        assertDoesNotThrow(() -> securityService.validateApiKey(validApiKey));
    }

    @Test
    void testValidateApiKey_WithInvalidKey_ShouldThrowException() {
        String invalidApiKey = "invalid-key";
        assertThrows(InvalidApiKeyException.class, () -> securityService.validateApiKey(invalidApiKey));
    }

    @Test
    void testValidateApiKey_WithNullKey_ShouldThrowException() {
        assertThrows(InvalidApiKeyException.class, () -> securityService.validateApiKey(null));
    }

    @Test
    void testValidateApiKey_WithEmptyKey_ShouldThrowException() {
        assertThrows(InvalidApiKeyException.class, () -> securityService.validateApiKey(""));
    }

    @Test
    void testValidateApiKey_WithBlankKey_ShouldThrowException() {
        assertThrows(InvalidApiKeyException.class, () -> securityService.validateApiKey("   "));
    }

    @Test
    void testGetValidApiKey_ShouldReturnCorrectKey() {
        String validKey = securityService.getValidApiKey();
        assertEquals("2f5ae96c-b558-4c7b-a590-a501ae1c3f6c", validKey);
    }

    @Test
    void testValidateApiKey_ExceptionMessage_ShouldNotContainActualKey() {
        try {
            securityService.validateApiKey("wrong-key");
            fail("Should have thrown InvalidApiKeyException");
        } catch (InvalidApiKeyException e) {
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().contains("wrong-key"));
        }
    }
}

