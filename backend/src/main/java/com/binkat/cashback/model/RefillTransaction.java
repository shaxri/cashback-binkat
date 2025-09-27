package com.binkat.cashback.model;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.enums.TransactionStatus;
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
@Table(name = "refill_transactions")
public class RefillTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    @NotNull
    private Station station;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_plan_id", nullable = false)
    @NotNull
    private PricePlan pricePlan;
    
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
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;
    
    @NotNull
    @DecimalMin("0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceUzs;
    
    @NotNull
    @DecimalMin("0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmountUzs;
    
    @DecimalMin("0")
    @Column(precision = 10, scale = 2)
    private BigDecimal pointsEarned;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    // Constructors
    public RefillTransaction() {}
    
    public RefillTransaction(User user, Station station, PricePlan pricePlan, 
                           FuelKind kind, Unit unit, BigDecimal quantity, BigDecimal unitPriceUzs) {
        this.user = user;
        this.station = station;
        this.pricePlan = pricePlan;
        this.kind = kind;
        this.unit = unit;
        this.quantity = quantity;
        this.unitPriceUzs = unitPriceUzs;
        this.totalAmountUzs = quantity.multiply(unitPriceUzs);
        this.status = TransactionStatus.PENDING;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Station getStation() {
        return station;
    }
    
    public void setStation(Station station) {
        this.station = station;
    }
    
    public PricePlan getPricePlan() {
        return pricePlan;
    }
    
    public void setPricePlan(PricePlan pricePlan) {
        this.pricePlan = pricePlan;
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
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPriceUzs() {
        return unitPriceUzs;
    }
    
    public void setUnitPriceUzs(BigDecimal unitPriceUzs) {
        this.unitPriceUzs = unitPriceUzs;
    }
    
    public BigDecimal getTotalAmountUzs() {
        return totalAmountUzs;
    }
    
    public void setTotalAmountUzs(BigDecimal totalAmountUzs) {
        this.totalAmountUzs = totalAmountUzs;
    }
    
    public BigDecimal getPointsEarned() {
        return pointsEarned;
    }
    
    public void setPointsEarned(BigDecimal pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(o instanceof RefillTransaction that)) return false;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "RefillTransaction{" +
                "id=" + id +
                ", kind=" + kind +
                ", grade=" + grade +
                ", quantity=" + quantity +
                ", totalAmountUzs=" + totalAmountUzs +
                ", pointsEarned=" + pointsEarned +
                ", status=" + status +
                '}';
    }
}