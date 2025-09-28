package com.binkat.cashback.dto.wallet;

import java.math.BigDecimal;
import java.time.Instant;

public record WalletBalanceResponse(
    Long walletId,
    BigDecimal currentBalance,
    String currency,
    Instant lastUpdated
) {}