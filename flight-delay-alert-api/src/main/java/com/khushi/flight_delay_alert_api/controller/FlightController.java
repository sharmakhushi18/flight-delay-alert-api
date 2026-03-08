package com.khushi.flight_delay_alert_api.controller;

import com.khushi.flight_delay_alert_api.model.Flight;
import com.khushi.flight_delay_alert_api.model.FlightStatus;
import com.khushi.flight_delay_alert_api.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Flight> getFlightStatus(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @PostMapping
    public ResponseEntity<Flight> addFlight(@Valid @RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.addFlight(flight));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Flight> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        FlightStatus status = FlightStatus.valueOf(
                payload.get("status").toString());
        int delayMinutes = payload.containsKey("delayMinutes") ?
                (int) payload.get("delayMinutes") : 0;
        return ResponseEntity.ok(
                flightService.updateFlightStatus(id, status, delayMinutes));
    }
}