package com.khushi.flight_delay_alert_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAlertEmail(String toEmail, String passengerName,
                               String flightNumber, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Flight Alert: " + flightNumber + " is now " + status);
        message.setText(
                "Dear " + passengerName + ",\n\n" +
                        "Your flight " + flightNumber + " status has been updated to: " + status + "\n\n" +
                        "Please check the latest updates.\n\n" +
                        "Regards,\nFlight Alert System"
        );
        mailSender.send(message);
    }
}