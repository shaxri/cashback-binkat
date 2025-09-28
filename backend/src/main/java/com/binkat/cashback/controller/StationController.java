package com.binkat.cashback.controller;

import com.binkat.cashback.dto.station.PricePlanResponse;
import com.binkat.cashback.dto.station.StationResponse;
import com.binkat.cashback.enums.FuelKind;
import com.binkat.cashback.enums.PetrolGrade;
import com.binkat.cashback.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "Stations", description = "Gas station and pricing endpoints")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/stations")
    @Operation(summary = "Get all stations", description = "Get list of all gas stations")
    public ResponseEntity<List<StationResponse>> getStations(
            @RequestParam(required = false) Boolean active) {
        List<StationResponse> stations = stationService.getAllStations(active);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/pricing/current")
    @Operation(summary = "Get current pricing", description = "Get current fuel pricing for a station")
    public ResponseEntity<PricePlanResponse> getCurrentPricing(
            @RequestParam Long stationId,
            @RequestParam FuelKind kind,
            @RequestParam(required = false) PetrolGrade grade) {
        PricePlanResponse pricing = stationService.getCurrentPricing(stationId, kind, grade);
        return ResponseEntity.ok(pricing);
    }
}