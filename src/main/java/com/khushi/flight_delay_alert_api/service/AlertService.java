package com.khushi.flight_delay_alert_api.service;

import com.khushi.flight_delay_alert_api.model.AlertNotification;
import com.khushi.flight_delay_alert_api.model.Flight;
import com.khushi.flight_delay_alert_api.model.Passenger;
import com.khushi.flight_delay_alert_api.repository.AlertRepository;
import com.khushi.flight_delay_alert_api.repository.PassengerRepository;
import com.khushi.flight_delay_alert_api.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;

    public List<AlertNotification> getAlertsByPassenger(Long passengerId) {
        passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found!"));
        return alertRepository.findByPassengerId(passengerId);
    }

    public AlertNotification subscribeAlert(Long passengerId, Long flightId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found!"));

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found!"));

        AlertNotification alert = new AlertNotification();
        alert.setPassenger(passenger);
        alert.setFlight(flight);
        alert.setMessage("Subscribed for alerts on flight: " + flight.getFlightNumber());
        alert.setTriggerStatus(flight.getStatus());

        return alertRepository.save(alert);
    }
}