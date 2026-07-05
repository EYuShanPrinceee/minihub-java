package com.minihub.health;

import com.minihub.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public ApiResponse<HealthResponse> health() {
        HealthResponse health = healthService.getHealth();
        return ApiResponse.success(health);
    }
    @GetMapping("/ping")
    public ApiResponse<String> ping() {
      return ApiResponse.success("pong");
    };
    @GetMapping("/info")
    public ApiResponse<HealthInfoResponse> info() {
        HealthInfoResponse healthInfoResponse=  healthService.getInfo();
        return ApiResponse.success(healthInfoResponse);
    };
}