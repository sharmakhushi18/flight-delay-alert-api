package com.khushi.flight_delay_alert_api.repository;

import com.khushi.flight_delay_alert_api.model.AlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<AlertNotification, Long> {
    List<AlertNotification> findByPassengerId(Long passengerId);
    List<AlertNotification> findByFlightId(Long flightId);
}