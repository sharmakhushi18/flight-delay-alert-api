package com.khushi.flight_delay_alert_api.repository;

import com.khushi.flight_delay_alert_api.model.Flight;
import com.khushi.flight_delay_alert_api.model.FlightStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByStatus(FlightStatus status);

    boolean existsByFlightNumber(String flightNumber);

    // Pessimistic Lock — only one transaction can update flight at a time
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Flight f WHERE f.id = :id")
    Optional<Flight> findByIdWithLock(@Param("id") Long id);
}