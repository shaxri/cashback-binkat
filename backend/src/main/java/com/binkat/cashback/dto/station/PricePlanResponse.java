package com.binkat.cashback.dto.station;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.enums.Unit;

import java.math.BigDecimal;
import java.time.Instant;

public record PricePlanResponse(
    Long id,
    Long stationId,
    String stationName,
    FuelKind kind,
    PetrolGrade grade,
    Unit unit,
    BigDecimal basePriceUzs,
    Instant effectiveFrom,
    Instant effectiveTo
) {}