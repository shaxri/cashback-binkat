package com.binkat.cashback.repository;

import com.binkat.cashback.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    
    List<Station> findByActiveTrue();
    
    List<Station> findByActive(Boolean active);
}