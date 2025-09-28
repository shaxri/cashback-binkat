package com.binkat.cashback.dto.wallet;

import com.binkat.cashback.enums.PointsReason;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record WalletLedgerResponse(
    List<LedgerEntry> entries,
    long totalCount,
    boolean hasMore,
    String nextCursor
) {
    public record LedgerEntry(
        Long id,
        PointsReason reason,
        BigDecimal pointsChange,
        BigDecimal balanceAfter,
        String description,
        Long refillTransactionId,
        Instant expiresAt,
        Instant createdAt
    ) {}
}