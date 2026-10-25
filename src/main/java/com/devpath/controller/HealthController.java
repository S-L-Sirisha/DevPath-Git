package com.devpath.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Value("${devpath.version:1.0.0}")
    private String version;

    @Value("${devpath.build-number:1}")
    private String buildNumber;

    @Value("${devpath.environment:local-development}")
    private String environment;

    private final DataSource dataSource;
    private final Environment env;
    private final AtomicBoolean isSimulatedDown = new AtomicBoolean(false);

    public HealthController(DataSource dataSource, Environment env) {
        this.dataSource = dataSource;
        this.env = env;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "DevPath Placement Portal");
        response.put("version", version);
        response.put("buildNumber", buildNumber);
        response.put("environment", environment);
        response.put("timestamp", LocalDateTime.now());
        response.put("uptimeMillis", ManagementFactory.getRuntimeMXBean().getUptime());
        response.put("activeProfiles", Arrays.asList(env.getActiveProfiles()));

        // Memory diagnostics
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("totalMemoryMb", runtime.totalMemory() / (1024 * 1024));
        memory.put("freeMemoryMb", runtime.freeMemory() / (1024 * 1024));
        memory.put("maxMemoryMb", runtime.maxMemory() / (1024 * 1024));
        response.put("memory", memory);

        // Database connectivity probe
        boolean dbHealthy = checkDatabaseHealth();
        response.put("databaseConnected", dbHealthy);

        if (isSimulatedDown.get()) {
            response.put("status", "DOWN (SIMULATED_FAILURE)");
            response.put("message", "Simulated unhealthy state for Kubernetes liveness/recovery demonstration.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        if (!dbHealthy) {
            response.put("status", "DEGRADED");
            response.put("message", "Database connection unreachable.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        response.put("status", "UP");
        response.put("message", "All internal systems operational.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simulate-failure")
    public ResponseEntity<Map<String, Object>> simulateFailure() {
        isSimulatedDown.set(true);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "SIMULATED_FAILURE_ACTIVATED");
        res.put("message", "Health probe is now returning 503 SERVICE UNAVAILABLE. Observe Kubernetes liveness restart!");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/simulate-recovery")
    public ResponseEntity<Map<String, Object>> simulateRecovery() {
        isSimulatedDown.set(false);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "RECOVERED");
        res.put("message", "Health probe reset to healthy UP state.");
        return ResponseEntity.ok(res);
    }

    private boolean checkDatabaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }
}
