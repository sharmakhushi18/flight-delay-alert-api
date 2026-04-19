package com.khushi.flight_delay_alert_api.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}