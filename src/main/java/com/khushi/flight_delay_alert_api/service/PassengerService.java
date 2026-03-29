package com.khushi.flight_delay_alert_api.service;

import com.khushi.flight_delay_alert_api.model.Passenger;
import com.khushi.flight_delay_alert_api.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + id));
    }

    public Passenger registerPassenger(Passenger passenger) {
        if (passengerRepository.existsByEmail(passenger.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }
        if (passengerRepository.existsByPassportNumber(passenger.getPassportNumber())) {
            throw new RuntimeException("Passport number already registered!");
        }
        return passengerRepository.save(passenger);
    }
}