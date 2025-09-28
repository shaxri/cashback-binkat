package com.binkat.cashback.dto.refill;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateRefillRequest(
    @NotNull
    Long stationId,
    
    @NotNull
    FuelKind kind,
    
    PetrolGrade grade,
    
    @NotNull
    @DecimalMin("0.01")
    BigDecimal quantity,
    
    String notes
) {}