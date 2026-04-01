package com.khushi.flight_delay_alert_api.service;

import com.khushi.flight_delay_alert_api.model.*;
import com.khushi.flight_delay_alert_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final AlertRepository alertRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
    }

    public Flight addFlight(Flight flight) {
        if (flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new RuntimeException("Flight number already exists!");
        }
        return flightRepository.save(flight);
    }

    @Transactional
    public Flight updateFlightStatus(Long id, FlightStatus newStatus, int delayMinutes) {
        Flight flight = getFlightById(id);

        validateStatusTransition(flight.getStatus(), newStatus);

        if (newStatus == FlightStatus.DELAYED && delayMinutes <= 0) {
            throw new RuntimeException("Delay minutes must be greater than 0 when status is DELAYED");
        }

        flight.setStatus(newStatus);
        flight.setDelayMinutes(newStatus == FlightStatus.DELAYED ? delayMinutes : 0);
        flightRepository.save(flight);

        if (newStatus == FlightStatus.DELAYED || newStatus == FlightStatus.CANCELLED) {
            triggerAlerts(flight);
        }

        return flight;
    }

    private void validateStatusTransition(FlightStatus current, FlightStatus next) {
        boolean valid = switch (current) {
            case ON_TIME -> next == FlightStatus.BOARDING ||
                    next == FlightStatus.DELAYED ||
                    next == FlightStatus.CANCELLED;
            case DELAYED -> next == FlightStatus.BOARDING ||
                    next == FlightStatus.CANCELLED;
            case BOARDING -> next == FlightStatus.DEPARTED;
            case DEPARTED, CANCELLED -> false;
        };
        if (!valid) {
            throw new RuntimeException("Invalid status transition: " + current + " → " + next);
        }
    }

    private void triggerAlerts(Flight flight) {
        List<Booking> bookings = bookingRepository.findByFlightId(flight.getId());
        bookings.forEach(booking -> {
            AlertNotification alert = new AlertNotification();
            alert.setPassenger(booking.getPassenger());
            alert.setFlight(flight);
            alert.setTriggerStatus(flight.getStatus());
            alert.setMessage("Your flight " + flight.getFlightNumber() +
                    " is now " + flight.getStatus() +
                    (flight.getStatus() == FlightStatus.DELAYED ?
                            " by " + flight.getDelayMinutes() + " minutes." : "."));
            alertRepository.save(alert);

            try {
                emailService.sendAlertEmail(
                        booking.getPassenger().getEmail(),
                        booking.getPassenger().getName(),
                        flight.getFlightNumber(),
                        flight.getStatus().toString()
                );
            } catch (Exception e) {
                System.err.println("Email failed for: " +
                        booking.getPassenger().getEmail() + " — " + e.getMessage());
            }
        });
    }
}