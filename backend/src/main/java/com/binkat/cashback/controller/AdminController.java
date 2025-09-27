package com.binkat.cashback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin analytics and management endpoints")
public class AdminController {

    @GetMapping("/analytics/overview")
    @Operation(summary = "Get analytics overview", description = "Get high-level analytics for admin dashboard")
    public ResponseEntity<Map<String, Object>> getAnalyticsOverview() {
        // TODO: Implement real analytics logic
        Map<String, Object> overview = Map.of(
            "totalCustomers", 0,
            "totalRefills", 0,
            "totalRevenue", BigDecimal.ZERO,
            "totalPointsIssued", BigDecimal.ZERO,
            "totalPointsRedeemed", BigDecimal.ZERO,
            "lastUpdated", Instant.now()
        );
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/analytics/top-customers")
    @Operation(summary = "Get top customers", description = "Get top customers by refills or volume")
    public ResponseEntity<Map<String, Object>> getTopCustomers() {
        // TODO: Implement real top customers logic
        Map<String, Object> topCustomers = Map.of(
            "topByRefills", java.util.List.of(),
            "topByVolume", java.util.List.of(),
            "lastUpdated", Instant.now()
        );
        return ResponseEntity.ok(topCustomers);
    }
}