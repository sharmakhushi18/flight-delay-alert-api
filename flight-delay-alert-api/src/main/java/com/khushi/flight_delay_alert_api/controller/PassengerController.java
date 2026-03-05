package com.khushi.flight_delay_alert_api.controller;

import com.khushi.flight_delay_alert_api.model.Passenger;
import com.khushi.flight_delay_alert_api.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @PostMapping
    public ResponseEntity<Passenger> registerPassenger(
            @Valid @RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.registerPassenger(passenger));
    }
}