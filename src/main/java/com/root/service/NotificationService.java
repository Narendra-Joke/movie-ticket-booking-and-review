package com.root.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.root.resource.TicketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private ObjectMapper objectMapper;


    public void sendNotification(TicketMessage message) {
        try {
            sendEmailToUser(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sendSMSToUser(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSMSToUser(TicketMessage message) {
        log.info("calling sms service for showDetails: {}  seatDetails : {}to number {}", message.getShow(), message.getSeats(), message.getMobile());
    }

    private void sendEmailToUser(TicketMessage message) throws JsonProcessingException {
        log.info("calling email service for showDetails: {}  seatDetails : {}to email {}", message.getShow(), message.getSeats(), message.getEmail());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject("MovieShark Tickets");
        mailMessage.setText("Show: " + message.getShow() + " Tickets: " + message.getSeats());

        log.info(objectMapper.writeValueAsString(message));

        javaMailSender.send(mailMessage);
    }
}
