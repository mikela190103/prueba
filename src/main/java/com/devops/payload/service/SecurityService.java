package com.devops.payload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devops.payload.exception.InvalidApiKeyException;

@Service
public class SecurityService {

    private static final String VALID_API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c3f6c";

    public void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new InvalidApiKeyException(null);
        }

        if (!VALID_API_KEY.equals(apiKey)) {
            throw new InvalidApiKeyException(apiKey);
        }
    }

    public String getValidApiKey() {
        return VALID_API_KEY;
    }
}

