package com.binkat.cashback.model;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.enums.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "price_plans", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"station_id", "kind", "grade", "unit", "effective_from"}))
public class PricePlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    @NotNull
    private Station station;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private FuelKind kind;
    
    @Enumerated(EnumType.STRING)
    private PetrolGrade grade;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Unit unit;
    
    @NotNull
    @DecimalMin("0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePriceUzs;
    
    @NotNull
    @Column(nullable = false)
    private Instant effectiveFrom;
    
    private Instant effectiveTo;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    // Constructors
    public PricePlan() {}
    
    public PricePlan(Station station, FuelKind kind, Unit unit, BigDecimal basePriceUzs, Instant effectiveFrom) {
        this.station = station;
        this.kind = kind;
        this.unit = unit;
        this.basePriceUzs = basePriceUzs;
        this.effectiveFrom = effectiveFrom;
        
        // Validation
        validateKindGradeUnit();
    }
    
    public PricePlan(Station station, FuelKind kind, PetrolGrade grade, Unit unit, BigDecimal basePriceUzs, Instant effectiveFrom) {
        this(station, kind, unit, basePriceUzs, effectiveFrom);
        this.grade = grade;
        
        // Re-validate
        validateKindGradeUnit();
    }
    
    private void validateKindGradeUnit() {
        // Petrol validation
        if (kind == FuelKind.PETROL) {
            if (grade == null) {
                throw new IllegalArgumentException("Grade is required for PETROL fuel kind");
            }
            if (unit != Unit.LITER) {
                throw new IllegalArgumentException("Unit must be LITER for PETROL fuel kind");
            }
        } else {
            if (grade != null) {
                throw new IllegalArgumentException("Grade must be null for non-PETROL fuel kinds");
            }
        }
        
        // Unit validation by fuel kind
        if (kind == FuelKind.DIESEL && unit != Unit.LITER) {
            throw new IllegalArgumentException("Unit must be LITER for DIESEL fuel kind");
        }
        if ((kind == FuelKind.EV_AC || kind == FuelKind.EV_DC) && unit != Unit.KWH) {
            throw new IllegalArgumentException("Unit must be KWH for EV fuel kinds");
        }
        // PROPANE can be LITER or KG (configurable)
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Station getStation() {
        return station;
    }
    
    public void setStation(Station station) {
        this.station = station;
    }
    
    public FuelKind getKind() {
        return kind;
    }
    
    public void setKind(FuelKind kind) {
        this.kind = kind;
    }
    
    public PetrolGrade getGrade() {
        return grade;
    }
    
    public void setGrade(PetrolGrade grade) {
        this.grade = grade;
    }
    
    public Unit getUnit() {
        return unit;
    }
    
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    public BigDecimal getBasePriceUzs() {
        return basePriceUzs;
    }
    
    public void setBasePriceUzs(BigDecimal basePriceUzs) {
        this.basePriceUzs = basePriceUzs;
    }
    
    public Instant getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void setEffectiveFrom(Instant effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public Instant getEffectiveTo() {
        return effectiveTo;
    }
    
    public void setEffectiveTo(Instant effectiveTo) {
        this.effectiveTo = effectiveTo;
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
        if (!(o instanceof PricePlan pricePlan)) return false;
        return Objects.equals(id, pricePlan.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "PricePlan{" +
                "id=" + id +
                ", kind=" + kind +
                ", grade=" + grade +
                ", unit=" + unit +
                ", basePriceUzs=" + basePriceUzs +
                ", effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                '}';
    }
}