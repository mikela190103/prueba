package com.devops.payload.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevOpsRequestTest {

    @Test
    void testDevOpsRequest_ConstructorWithParameters() {
        DevOpsRequest request = new DevOpsRequest("Test message", "Juan Perez", "Rita Asturia", 45);

        assertEquals("Test message", request.getMessage());
        assertEquals("Juan Perez", request.getTo());
        assertEquals("Rita Asturia", request.getFrom());
        assertEquals(45, request.getTimeToLifeSec());
    }

    @Test
    void testDevOpsRequest_DefaultConstructor() {
        DevOpsRequest request = new DevOpsRequest();

        assertNull(request.getMessage());
        assertNull(request.getTo());
        assertNull(request.getFrom());
        assertNull(request.getTimeToLifeSec());
    }

    @Test
    void testDevOpsRequest_Setters() {
        DevOpsRequest request = new DevOpsRequest();
        request.setMessage("New message");
        request.setTo("New recipient");
        request.setFrom("New sender");
        request.setTimeToLifeSec(60);

        assertEquals("New message", request.getMessage());
        assertEquals("New recipient", request.getTo());
        assertEquals("New sender", request.getFrom());
        assertEquals(60, request.getTimeToLifeSec());
    }

    @Test
    void testDevOpsRequest_Equals_SameObjects() {
        DevOpsRequest request1 = new DevOpsRequest("Message", "To", "From", 45);
        DevOpsRequest request2 = new DevOpsRequest("Message", "To", "From", 45);

        assertEquals(request1, request2);
    }

    @Test
    void testDevOpsRequest_Equals_DifferentObjects() {
        DevOpsRequest request1 = new DevOpsRequest("Message", "To", "From", 45);
        DevOpsRequest request2 = new DevOpsRequest("Different", "To", "From", 45);

        assertNotEquals(request1, request2);
    }

    @Test
    void testDevOpsRequest_Equals_WithNull() {
        DevOpsRequest request = new DevOpsRequest("Message", "To", "From", 45);

        assertNotEquals(request, null);
    }

    @Test
    void testDevOpsRequest_Equals_SameInstance() {
        DevOpsRequest request = new DevOpsRequest("Message", "To", "From", 45);

        assertEquals(request, request);
    }

    @Test
    void testDevOpsRequest_Equals_DifferentClass() {
        DevOpsRequest request = new DevOpsRequest("Message", "To", "From", 45);
        String other = "String";

        assertNotEquals(request, other);
    }

    @Test
    void testDevOpsRequest_HashCode_EqualObjects() {
        DevOpsRequest request1 = new DevOpsRequest("Message", "To", "From", 45);
        DevOpsRequest request2 = new DevOpsRequest("Message", "To", "From", 45);

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testDevOpsRequest_ToString() {
        DevOpsRequest request = new DevOpsRequest("Test message", "Juan Perez", "Rita Asturia", 45);
        String toString = request.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains("Juan Perez"));
        assertTrue(toString.contains("Rita Asturia"));
        assertTrue(toString.contains("45"));
    }

    @Test
    void testDevOpsRequest_EqualsWithNullFields() {
        DevOpsRequest request1 = new DevOpsRequest(null, null, null, null);
        DevOpsRequest request2 = new DevOpsRequest(null, null, null, null);

        assertEquals(request1, request2);
    }

    @Test
    void testDevOpsRequest_PartialNullFields() {
        DevOpsRequest request1 = new DevOpsRequest("Message", null, "From", 45);
        DevOpsRequest request2 = new DevOpsRequest("Message", "To", "From", 45);

        assertNotEquals(request1, request2);
    }
}

