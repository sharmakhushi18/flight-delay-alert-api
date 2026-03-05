package com.khushi.flight_delay_alert_api.model;

public enum FlightStatus {
    ON_TIME,     // Flight operating as scheduled
    DELAYED,     // Flight delayed, delayMinutes > 0
    CANCELLED,   // Flight cancelled
    BOARDING,    // Boarding in progress
    DEPARTED     // Flight has left
}