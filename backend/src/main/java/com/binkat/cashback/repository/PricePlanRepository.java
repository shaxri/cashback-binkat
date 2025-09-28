package com.binkat.cashback.repository;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.model.PricePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PricePlanRepository extends JpaRepository<PricePlan, Long> {
    
    @Query("SELECT pp FROM PricePlan pp WHERE pp.station.id = :stationId " +
           "AND pp.kind = :kind " +
           "AND (:grade IS NULL OR pp.grade = :grade) " +
           "AND pp.effectiveFrom <= :now " +
           "AND (pp.effectiveTo IS NULL OR pp.effectiveTo > :now) " +
           "ORDER BY pp.effectiveFrom DESC")
    Optional<PricePlan> findCurrentPricing(@Param("stationId") Long stationId,
                                         @Param("kind") FuelKind kind,
                                         @Param("grade") PetrolGrade grade,
                                         @Param("now") Instant now);
}