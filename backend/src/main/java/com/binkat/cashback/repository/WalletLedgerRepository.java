package com.binkat.cashback.repository;

import com.binkat.cashback.model.Wallet;
import com.binkat.cashback.model.WalletLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WalletLedgerRepository extends JpaRepository<WalletLedger, Long> {
    
    Page<WalletLedger> findByWalletOrderByCreatedAtDesc(Wallet wallet, Pageable pageable);
    
    Page<WalletLedger> findByWalletIdOrderByCreatedAtDesc(Long walletId, Pageable pageable);
    
    @Query("SELECT SUM(wl.pointsChange) FROM WalletLedger wl WHERE wl.wallet.id = :walletId")
    BigDecimal calculateCurrentBalance(@Param("walletId") Long walletId);
}