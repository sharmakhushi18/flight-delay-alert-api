package com.khushi.flight_delay_alert_api.service;

import com.khushi.flight_delay_alert_api.model.*;
import com.khushi.flight_delay_alert_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    @Transactional
    public Booking bookFlight(Long flightId, Long passengerId, String seatNumber) {

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found!"));

        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found!"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available!");
        }

        if (bookingRepository.existsByFlightIdAndSeatNumber(flightId, seatNumber)) {
            throw new RuntimeException("Seat " + seatNumber + " already booked!");
        }

        if (flight.getStatus() == FlightStatus.CANCELLED ||
                flight.getStatus() == FlightStatus.DEPARTED) {
            throw new RuntimeException("Cannot book. Flight is " + flight.getStatus());
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setSeatNumber(seatNumber);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled!");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }
}