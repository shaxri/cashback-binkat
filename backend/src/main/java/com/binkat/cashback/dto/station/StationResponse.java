package com.binkat.cashback.dto.station;

import com.binkat.cashback.enums.StationType;

import java.math.BigDecimal;
import java.time.Instant;

public record StationResponse(
    Long id,
    String name,
    StationType type,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    Boolean active,
    Instant createdAt
) {}