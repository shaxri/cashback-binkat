package com.binkat.cashback.controller;

import com.binkat.cashback.dto.refill.CreateRefillRequest;
import com.binkat.cashback.dto.refill.RefillTransactionResponse;
import com.binkat.cashback.service.RefillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refills")
@Tag(name = "Refills", description = "Fuel refill transaction endpoints")
public class RefillController {

    private final RefillService refillService;

    public RefillController(RefillService refillService) {
        this.refillService = refillService;
    }

    @PostMapping
    @Operation(summary = "Create refill transaction", description = "Record a new fuel refill transaction")
    public ResponseEntity<RefillTransactionResponse> createRefill(
            HttpServletRequest request,
            @Valid @RequestBody CreateRefillRequest refillRequest) {
        Long userId = (Long) request.getAttribute("userId");
        RefillTransactionResponse response = refillService.createRefill(userId, refillRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get refill transactions", description = "Get refill transaction history for authenticated user")
    public ResponseEntity<List<RefillTransactionResponse>> getRefills(
            HttpServletRequest request,
            @RequestParam(defaultValue = "50") int limit) {
        Long userId = (Long) request.getAttribute("userId");
        List<RefillTransactionResponse> refills = refillService.getRefills(userId, limit);
        return ResponseEntity.ok(refills);
    }
}