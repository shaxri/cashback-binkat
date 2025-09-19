package com.binkat.cashback.model;

import com.binkat.cashback.enums.TierCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tiers")
public class Tier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, unique = true)
    private TierCode code;
    
    @NotNull
    @DecimalMin("0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal thresholdPoints90d;
    
    @NotNull
    @DecimalMin("1.0")
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal multiplier;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    // Constructors
    public Tier() {}
    
    public Tier(TierCode code, BigDecimal thresholdPoints90d, BigDecimal multiplier) {
        this.code = code;
        this.thresholdPoints90d = thresholdPoints90d;
        this.multiplier = multiplier;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public TierCode getCode() {
        return code;
    }
    
    public void setCode(TierCode code) {
        this.code = code;
    }
    
    public BigDecimal getThresholdPoints90d() {
        return thresholdPoints90d;
    }
    
    public void setThresholdPoints90d(BigDecimal thresholdPoints90d) {
        this.thresholdPoints90d = thresholdPoints90d;
    }
    
    public BigDecimal getMultiplier() {
        return multiplier;
    }
    
    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tier tier)) return false;
        return Objects.equals(id, tier.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Tier{" +
                "id=" + id +
                ", code=" + code +
                ", thresholdPoints90d=" + thresholdPoints90d +
                ", multiplier=" + multiplier +
                '}';
    }
}