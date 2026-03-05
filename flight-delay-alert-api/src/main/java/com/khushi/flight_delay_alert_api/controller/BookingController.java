package com.khushi.flight_delay_alert_api.controller;

import com.khushi.flight_delay_alert_api.model.Booking;
import com.khushi.flight_delay_alert_api.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> bookFlight(
            @RequestBody Map<String, Object> payload) {

        Long flightId = Long.valueOf(payload.get("flightId").toString());
        Long passengerId = Long.valueOf(payload.get("passengerId").toString());
        String seatNumber = payload.get("seatNumber").toString();

        return ResponseEntity.ok(
                bookingService.bookFlight(flightId, passengerId, seatNumber));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<Booking>> getBookingsByPassenger(
            @PathVariable Long passengerId) {
        return ResponseEntity.ok(
                bookingService.getBookingsByPassenger(passengerId));
    }
}