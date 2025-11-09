package com.devops.payload.service;

import org.springframework.stereotype.Service;

import com.devops.payload.exception.InvalidJwtException;

@Service
public class JwtService {

    public void validateJwt(String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            throw new InvalidJwtException(null);
        }

        if (!isValidJwtFormat(jwt)) {
            throw new InvalidJwtException(jwt);
        }
    }

    private boolean isValidJwtFormat(String jwt) {
        // JWT should have at least 3 parts separated by dots
        return jwt.split("\\.").length >= 3;
    }
}

