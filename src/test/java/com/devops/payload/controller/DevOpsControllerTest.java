package com.devops.payload.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.devops.payload.model.DevOpsRequest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DevOpsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String VALID_API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c3f6c";
    private static final String VALID_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";

    @Test
    void testHandleDevOpsRequest_WithValidRequest_ShouldReturnSuccess() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Hello Juan Perez your message will be send")));
    }

    @Test
    void testHandleDevOpsRequest_WithoutApiKey_ShouldReturnUnauthorized() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void testHandleDevOpsRequest_WithInvalidApiKey_ShouldReturnUnauthorized() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", "invalid-key")
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void testHandleDevOpsRequest_WithoutJwt_ShouldReturnUnauthorized() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void testHandleDevOpsRequest_WithInvalidJwt_ShouldReturnUnauthorized() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", "invalid-jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void testHandleDevOpsRequest_WithMissingMessageField_ShouldReturnBadRequest() throws Exception {
        String jsonWithoutMessage = "{\"to\": \"Juan Perez\", \"from\": \"Rita Asturia\", \"timeToLifeSec\": 45}";

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithoutMessage))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_WithMissingToField_ShouldReturnBadRequest() throws Exception {
        String jsonWithoutTo = "{\"message\": \"Test\", \"from\": \"Rita Asturia\", \"timeToLifeSec\": 45}";

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithoutTo))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_WithMissingFromField_ShouldReturnBadRequest() throws Exception {
        String jsonWithoutFrom = "{\"message\": \"Test\", \"to\": \"Juan Perez\", \"timeToLifeSec\": 45}";

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithoutFrom))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_WithMissingTimeToLifeSecField_ShouldReturnBadRequest() throws Exception {
        String jsonWithoutTimeToLifeSec = "{\"message\": \"Test\", \"to\": \"Juan Perez\", \"from\": \"Rita Asturia\"}";

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithoutTimeToLifeSec))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_WithNegativeTimeToLifeSec_ShouldReturnBadRequest() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", -1);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_GetMethod_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testHandleDevOpsRequest_PutMethod_ShouldReturnError() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(put("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testHandleDevOpsRequest_PatchMethod_ShouldReturnError() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(patch("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testHandleDevOpsRequest_DeleteMethod_ShouldReturnError() throws Exception {
        mockMvc.perform(delete("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testHandleDevOpsRequest_WithBlankMessage_ShouldReturnBadRequest() throws Exception {
        DevOpsRequest request = new DevOpsRequest("", "Juan Perez", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_WithBlankTo_ShouldReturnBadRequest() throws Exception {
        DevOpsRequest request = new DevOpsRequest("Message", "", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_WithBlankFrom_ShouldReturnBadRequest() throws Exception {
        DevOpsRequest request = new DevOpsRequest("Message", "Juan Perez", "", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleDevOpsRequest_ValidRequestWithDifferentRecipient_ShouldIncludeRecipientName() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "Maria Garcia", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Maria Garcia")));
    }

    @Test
    void testHandleDevOpsRequest_ResponseMessageFormat_ShouldBeCorrect() throws Exception {
        DevOpsRequest request = new DevOpsRequest("This is a test", "TestUser", "Rita Asturia", 45);

        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Hello ")))
                .andExpect(jsonPath("$.message", containsString(" your message will be send")));
    }

    @Test
    void testHandleDevOpsRequest_WithEmptyJsonBody_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/DevOps")
                .header("X-Parse-REST-API-Key", VALID_API_KEY)
                .header("X-JWT-KWY", VALID_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

