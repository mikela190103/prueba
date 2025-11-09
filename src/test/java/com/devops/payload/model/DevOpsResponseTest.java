package com.devops.payload.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevOpsResponseTest {

    @Test
    void testDevOpsResponse_ConstructorWithParameter() {
        DevOpsResponse response = new DevOpsResponse("Hello Juan Perez your message will be send");

        assertEquals("Hello Juan Perez your message will be send", response.getMessage());
    }

    @Test
    void testDevOpsResponse_DefaultConstructor() {
        DevOpsResponse response = new DevOpsResponse();

        assertNull(response.getMessage());
    }

    @Test
    void testDevOpsResponse_Setter() {
        DevOpsResponse response = new DevOpsResponse();
        response.setMessage("New message");

        assertEquals("New message", response.getMessage());
    }

    @Test
    void testDevOpsResponse_Equals_SameObjects() {
        DevOpsResponse response1 = new DevOpsResponse("Message");
        DevOpsResponse response2 = new DevOpsResponse("Message");

        assertEquals(response1, response2);
    }

    @Test
    void testDevOpsResponse_Equals_DifferentObjects() {
        DevOpsResponse response1 = new DevOpsResponse("Message");
        DevOpsResponse response2 = new DevOpsResponse("Different");

        assertNotEquals(response1, response2);
    }

    @Test
    void testDevOpsResponse_Equals_WithNull() {
        DevOpsResponse response = new DevOpsResponse("Message");

        assertNotEquals(response, null);
    }

    @Test
    void testDevOpsResponse_Equals_SameInstance() {
        DevOpsResponse response = new DevOpsResponse("Message");

        assertEquals(response, response);
    }

    @Test
    void testDevOpsResponse_Equals_DifferentClass() {
        DevOpsResponse response = new DevOpsResponse("Message");
        String other = "String";

        assertNotEquals(response, other);
    }

    @Test
    void testDevOpsResponse_HashCode_EqualObjects() {
        DevOpsResponse response1 = new DevOpsResponse("Message");
        DevOpsResponse response2 = new DevOpsResponse("Message");

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testDevOpsResponse_ToString() {
        DevOpsResponse response = new DevOpsResponse("Hello Juan Perez your message will be send");
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Hello Juan Perez your message will be send"));
    }

    @Test
    void testDevOpsResponse_EqualsWithNullMessage() {
        DevOpsResponse response1 = new DevOpsResponse(null);
        DevOpsResponse response2 = new DevOpsResponse(null);

        assertEquals(response1, response2);
    }
}

