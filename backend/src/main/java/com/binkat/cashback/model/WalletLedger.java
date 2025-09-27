package com.binkat.cashback.model;

import com.binkat.cashback.enums.PointsReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "wallet_ledger")
public class WalletLedger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    @NotNull
    private Wallet wallet;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refill_transaction_id")
    private RefillTransaction refillTransaction;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private PointsReason reason;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pointsChange;
    
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balanceAfter;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private Instant expiresAt;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    // Constructors
    public WalletLedger() {}
    
    public WalletLedger(Wallet wallet, PointsReason reason, BigDecimal pointsChange, 
                       BigDecimal balanceAfter, String description) {
        this.wallet = wallet;
        this.reason = reason;
        this.pointsChange = pointsChange;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }
    
    public WalletLedger(Wallet wallet, RefillTransaction refillTransaction, PointsReason reason, 
                       BigDecimal pointsChange, BigDecimal balanceAfter, String description) {
        this(wallet, reason, pointsChange, balanceAfter, description);
        this.refillTransaction = refillTransaction;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Wallet getWallet() {
        return wallet;
    }
    
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
    
    public RefillTransaction getRefillTransaction() {
        return refillTransaction;
    }
    
    public void setRefillTransaction(RefillTransaction refillTransaction) {
        this.refillTransaction = refillTransaction;
    }
    
    public PointsReason getReason() {
        return reason;
    }
    
    public void setReason(PointsReason reason) {
        this.reason = reason;
    }
    
    public BigDecimal getPointsChange() {
        return pointsChange;
    }
    
    public void setPointsChange(BigDecimal pointsChange) {
        this.pointsChange = pointsChange;
    }
    
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Instant getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WalletLedger that)) return false;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "WalletLedger{" +
                "id=" + id +
                ", reason=" + reason +
                ", pointsChange=" + pointsChange +
                ", balanceAfter=" + balanceAfter +
                ", createdAt=" + createdAt +
                '}';
    }
}