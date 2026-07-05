package com.minihub.health;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthResponse getHealth() {
        return new HealthResponse("ok", "minihub-java","1.0.0");
    }
    public HealthInfoResponse getInfo() {
        return new HealthInfoResponse("MiniHub Java","Spring Boot Backend","eyu");
    }
}