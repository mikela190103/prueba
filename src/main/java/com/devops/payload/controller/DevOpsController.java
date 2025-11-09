package com.devops.payload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devops.payload.exception.InvalidApiKeyException;
import com.devops.payload.exception.InvalidJwtException;
import com.devops.payload.model.DevOpsRequest;
import com.devops.payload.model.DevOpsResponse;
import com.devops.payload.service.DevOpsService;
import com.devops.payload.service.JwtService;
import com.devops.payload.service.SecurityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/DevOps")
public class DevOpsController {

    private final SecurityService securityService;
    private final JwtService jwtService;
    private final DevOpsService devOpsService;

    public DevOpsController(SecurityService securityService, JwtService jwtService, DevOpsService devOpsService) {
        this.securityService = securityService;
        this.jwtService = jwtService;
        this.devOpsService = devOpsService;
    }

    @PostMapping
    public ResponseEntity<DevOpsResponse> handleDevOpsRequest(
            @RequestHeader(value = "X-Parse-REST-API-Key", required = false) String apiKey,
            @RequestHeader(value = "X-JWT-KWY", required = false) String jwt,
            @Valid @RequestBody DevOpsRequest request) {

        securityService.validateApiKey(apiKey);
        jwtService.validateJwt(jwt);

        DevOpsResponse response = devOpsService.processDevOpsRequest(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler({InvalidApiKeyException.class, InvalidJwtException.class})
    public ResponseEntity<String> handleSecurityException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR");
    }
}

