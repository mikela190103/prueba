package com.devops.payload.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devops.payload.model.DevOpsRequest;
import com.devops.payload.model.DevOpsResponse;

import static org.junit.jupiter.api.Assertions.*;

class DevOpsServiceTest {

    private DevOpsService devOpsService;

    @BeforeEach
    void setUp() {
        devOpsService = new DevOpsService();
    }

    @Test
    void testProcessDevOpsRequest_WithValidRequest_ShouldReturnCorrectResponse() {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);
        DevOpsResponse response = devOpsService.processDevOpsRequest(request);

        assertNotNull(response);
        assertEquals("Hello Juan Perez your message will be send", response.getMessage());
    }

    @Test
    void testProcessDevOpsRequest_WithDifferentRecipient_ShouldIncludeRecipientNameInResponse() {
        DevOpsRequest request = new DevOpsRequest("Message", "Maria Garcia", "John Doe", 30);
        DevOpsResponse response = devOpsService.processDevOpsRequest(request);

        assertEquals("Hello Maria Garcia your message will be send", response.getMessage());
    }

    @Test
    void testProcessDevOpsRequest_ShouldNotBeNull() {
        DevOpsRequest request = new DevOpsRequest("Test message", "Test User", "Sender", 60);
        DevOpsResponse response = devOpsService.processDevOpsRequest(request);

        assertNotNull(response);
        assertNotNull(response.getMessage());
    }

    @Test
    void testProcessDevOpsRequest_MessageFormatIsCorrect() {
        DevOpsRequest request = new DevOpsRequest("Any message", "TestRecipient", "TestSender", 100);
        DevOpsResponse response = devOpsService.processDevOpsRequest(request);

        assertTrue(response.getMessage().startsWith("Hello "));
        assertTrue(response.getMessage().endsWith(" your message will be send"));
        assertTrue(response.getMessage().contains("TestRecipient"));
    }
}

