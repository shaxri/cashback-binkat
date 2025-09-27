package com.binkat.cashback.controller;

import com.binkat.cashback.dto.wallet.WalletBalanceResponse;
import com.binkat.cashback.dto.wallet.WalletLedgerResponse;
import com.binkat.cashback.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@Tag(name = "Wallet", description = "Wallet and points management endpoints")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    @Operation(summary = "Get wallet balance", description = "Get current points balance for authenticated user")
    public ResponseEntity<WalletBalanceResponse> getBalance(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        WalletBalanceResponse response = walletService.getBalance(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ledger")
    @Operation(summary = "Get wallet ledger", description = "Get transaction history for authenticated user")
    public ResponseEntity<WalletLedgerResponse> getLedger(
            HttpServletRequest request,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String cursor) {
        Long userId = (Long) request.getAttribute("userId");
        WalletLedgerResponse response = walletService.getLedger(userId, limit, cursor);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/redeem")
    @Operation(summary = "Redeem points", description = "Redeem points for discounts")
    public ResponseEntity<String> redeemPoints(HttpServletRequest request) {
        // TODO: Implement points redemption logic
        return ResponseEntity.ok("Points redemption not yet implemented");
    }
}