package com.khushi.flight_delay_alert_api.repository;

import com.khushi.flight_delay_alert_api.model.Flight;
import com.khushi.flight_delay_alert_api.model.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByStatus(FlightStatus status);

    boolean existsByFlightNumber(String flightNumber);
}