package com.binkat.cashback.model;

import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "pump_or_chargers")
public class PumpOrCharger {
    
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
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    // Constructors
    public PumpOrCharger() {}
    
    public PumpOrCharger(Station station, FuelKind kind, String code) {
        this.station = station;
        this.kind = kind;
        this.code = code;
        this.active = true;
        
        // Validation: grade required for PETROL, null for others
        if (kind == FuelKind.PETROL && grade == null) {
            throw new IllegalArgumentException("Grade is required for PETROL fuel kind");
        }
        if (kind != FuelKind.PETROL && grade != null) {
            throw new IllegalArgumentException("Grade must be null for non-PETROL fuel kinds");
        }
    }
    
    public PumpOrCharger(Station station, FuelKind kind, PetrolGrade grade, String code) {
        this(station, kind, code);
        this.grade = grade;
        
        // Re-validate after setting grade
        if (kind == FuelKind.PETROL && grade == null) {
            throw new IllegalArgumentException("Grade is required for PETROL fuel kind");
        }
        if (kind != FuelKind.PETROL && grade != null) {
            throw new IllegalArgumentException("Grade must be null for non-PETROL fuel kinds");
        }
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof PumpOrCharger that)) return false;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "PumpOrCharger{" +
                "id=" + id +
                ", kind=" + kind +
                ", grade=" + grade +
                ", code='" + code + '\'' +
                ", active=" + active +
                '}';
    }
}