package com.minihub.health;

public class HealthResponse {
    private String status;
    private String service;
    private String version;
    public HealthResponse(String status, String service, String version) {
        this.status = status;
        this.service = service;
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public String getService() {
        return service;
    }
    public String getVersion() {
        return version;
    }
}