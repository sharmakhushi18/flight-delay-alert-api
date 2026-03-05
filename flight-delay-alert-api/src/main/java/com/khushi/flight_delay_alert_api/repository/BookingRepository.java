package com.khushi.flight_delay_alert_api.repository;

import com.khushi.flight_delay_alert_api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPassengerId(Long passengerId);
    List<Booking> findByFlightId(Long flightId);
    boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber);
}