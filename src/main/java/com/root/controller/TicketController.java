package com.root.controller;

import com.root.resource.BookingResource;
import com.root.resource.TicketResource;
import com.root.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/book")
    public ResponseEntity<TicketResource> bookTicket(@RequestBody BookingResource bookingResource) {
        log.info("Received Request to book ticket: " + bookingResource);
        return ResponseEntity.ok(ticketService.bookTicket(bookingResource));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResource> getTicket(@PathVariable(name = "id") @Min(value = 1, message = "Ticket Id Cannot be -ve") long id) {
        log.info("Received Request to get ticket: " + id);
        return ResponseEntity.ok(ticketService.getTicket(id));
    }
}

