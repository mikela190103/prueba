package com.devops.payload.service;

import org.springframework.stereotype.Service;

import com.devops.payload.model.DevOpsRequest;
import com.devops.payload.model.DevOpsResponse;

@Service
public class DevOpsService {

    public DevOpsResponse processDevOpsRequest(DevOpsRequest request) {
        String recipientName = request.getTo();
        String message = String.format("Hello %s your message will be send", recipientName);
        return new DevOpsResponse(message);
    }
}

