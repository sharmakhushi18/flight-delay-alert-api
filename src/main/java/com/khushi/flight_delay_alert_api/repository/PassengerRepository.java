package com.khushi.flight_delay_alert_api.repository;

import com.khushi.flight_delay_alert_api.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    boolean existsByEmail(String email);

    boolean existsByPassportNumber(String passportNumber);
}