package com.khushi.flight_delay_alert_api.config;

import com.khushi.flight_delay_alert_api.model.Flight;
import com.khushi.flight_delay_alert_api.model.FlightStatus;
import com.khushi.flight_delay_alert_api.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FlightRepository flightRepository;

    public DataSeeder(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) {
        if (flightRepository.count() > 0) {
            System.out.println("⏭️ Flights already exist, skipping...");
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<Flight> flights = List.of(
                createFlight("AI202","DEL","BOM", now.plusHours(2), 180, 42, FlightStatus.ON_TIME,   0),
                createFlight("UK456","BOM","DEL", now.plusHours(1), 150, 30, FlightStatus.DELAYED,  45),
                createFlight("SG101","BLR","DEL", now.plusHours(5), 200, 25, FlightStatus.DELAYED,  30),
                createFlight("AI910","HYD","BOM", now.plusHours(3), 170,  0, FlightStatus.CANCELLED, 0),
                createFlight("6E789","DEL","CCU", now.plusHours(4), 180, 55, FlightStatus.ON_TIME,   0),
                createFlight("UK321","DEL","BLR", now.plusHours(1), 190, 10, FlightStatus.BOARDING,  0),
                createFlight("SG202","CCU","DEL", now.minusHours(1),160,  0, FlightStatus.DEPARTED,  0),
                createFlight("6E555","BOM","BLR", now.plusHours(6), 175, 80, FlightStatus.ON_TIME,   0),
                createFlight("AI777","BLR","HYD", now.plusHours(7), 165,  0, FlightStatus.CANCELLED, 0),
                createFlight("AI305","BOM","HYD", now.plusHours(8), 160, 60, FlightStatus.ON_TIME,   0)
        );

        flightRepository.saveAll(flights);
        System.out.println("✅ Seeded 10 flights successfully!");
    }

    private Flight createFlight(String num, String src, String dest,
                                LocalDateTime time, int total, int available,
                                FlightStatus status, int delay) {
        Flight f = new Flight();
        f.setFlightNumber(num);
        f.setSource(src);
        f.setDestination(dest);
        f.setDepartureTime(time);
        f.setTotalSeats(total);
        f.setAvailableSeats(available);
        f.setStatus(status);
        f.setDelayMinutes(delay);
        return f;
    }
}