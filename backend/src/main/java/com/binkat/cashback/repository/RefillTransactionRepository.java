package com.binkat.cashback.repository;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.model.RefillTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RefillTransactionRepository extends JpaRepository<RefillTransaction, Long> {
    
    Page<RefillTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    @Query("SELECT rt FROM RefillTransaction rt WHERE rt.user.id = :userId " +
           "AND (:from IS NULL OR rt.createdAt >= :from) " +
           "AND (:to IS NULL OR rt.createdAt <= :to) " +
           "AND (:kind IS NULL OR rt.kind = :kind) " +
           "AND (:grade IS NULL OR rt.grade = :grade) " +
           "AND (:stationId IS NULL OR rt.station.id = :stationId) " +
           "ORDER BY rt.createdAt DESC")
    Page<RefillTransaction> findByUserIdWithFilters(@Param("userId") Long userId,
                                                   @Param("from") Instant from,
                                                   @Param("to") Instant to,
                                                   @Param("kind") FuelKind kind,
                                                   @Param("grade") PetrolGrade grade,
                                                   @Param("stationId") Long stationId,
                                                   Pageable pageable);
    
    @Query("SELECT COUNT(rt) FROM RefillTransaction rt WHERE rt.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(rt.quantity) FROM RefillTransaction rt WHERE rt.user.id = :userId AND rt.kind = :kind")
    Double sumQuantityByUserIdAndKind(@Param("userId") Long userId, @Param("kind") FuelKind kind);
}