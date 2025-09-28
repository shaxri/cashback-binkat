package com.binkat.cashback.dto.refill;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.enums.TransactionStatus;
import com.binkat.cashback.enums.Unit;

import java.math.BigDecimal;
import java.time.Instant;

public record RefillTransactionResponse(
    Long id,
    Long userId,
    Long stationId,
    String stationName,
    FuelKind kind,
    PetrolGrade grade,
    Unit unit,
    BigDecimal quantity,
    BigDecimal unitPriceUzs,
    BigDecimal totalAmountUzs,
    BigDecimal pointsEarned,
    TransactionStatus status,
    String notes,
    Instant createdAt
) {}