package com.binkat.cashback.service;

import com.binkat.cashback.dto.station.PricePlanResponse;
import com.binkat.cashback.dto.station.StationResponse;
import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.model.PricePlan;
import com.binkat.cashback.model.Station;
import com.binkat.cashback.repository.PricePlanRepository;
import com.binkat.cashback.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;
    private final PricePlanRepository pricePlanRepository;

    public StationService(StationRepository stationRepository,
                         PricePlanRepository pricePlanRepository) {
        this.stationRepository = stationRepository;
        this.pricePlanRepository = pricePlanRepository;
    }

    public List<StationResponse> getAllStations(Boolean active) {
        List<Station> stations = active != null ? 
            stationRepository.findByActive(active) : 
            stationRepository.findAll();

        return stations.stream()
            .map(this::mapToStationResponse)
            .toList();
    }

    public PricePlanResponse getCurrentPricing(Long stationId, FuelKind kind, PetrolGrade grade) {
        PricePlan pricePlan = pricePlanRepository.findCurrentPricing(
            stationId, kind, grade, Instant.now()
        ).orElseThrow(() -> new IllegalArgumentException(
            "No current pricing found for station " + stationId + 
            ", kind " + kind + 
            (grade != null ? ", grade " + grade : "")));

        return mapToPricePlanResponse(pricePlan);
    }

    private StationResponse mapToStationResponse(Station station) {
        return new StationResponse(
            station.getId(),
            station.getName(),
            station.getType(),
            station.getAddress(),
            station.getLatitude(),
            station.getLongitude(),
            station.getActive(),
            station.getCreatedAt()
        );
    }

    private PricePlanResponse mapToPricePlanResponse(PricePlan pricePlan) {
        return new PricePlanResponse(
            pricePlan.getId(),
            pricePlan.getStation().getId(),
            pricePlan.getStation().getName(),
            pricePlan.getKind(),
            pricePlan.getGrade(),
            pricePlan.getUnit(),
            pricePlan.getBasePriceUzs(),
            pricePlan.getEffectiveFrom(),
            pricePlan.getEffectiveTo()
        );
    }
}