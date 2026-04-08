package com.khushi.flight_delay_alert_api.controller;

import com.khushi.flight_delay_alert_api.model.AlertNotification;
import com.khushi.flight_delay_alert_api.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/subscribe")
    public ResponseEntity<AlertNotification> subscribe(
            @RequestBody Map<String, Object> payload) {

        Long passengerId = Long.valueOf(payload.get("passengerId").toString());
        Long flightId = Long.valueOf(payload.get("flightId").toString());

        return ResponseEntity.ok(
                alertService.subscribeAlert(passengerId, flightId));
    }

    @GetMapping("/{passengerId}")
    public ResponseEntity<List<AlertNotification>> getAlerts(
            @PathVariable Long passengerId) {
        return ResponseEntity.ok(
                alertService.getAlertsByPassenger(passengerId));
    }
}