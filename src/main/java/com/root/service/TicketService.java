package com.root.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.root.domain.Show;
import com.root.domain.ShowSeat;
import com.root.domain.Ticket;
import com.root.domain.User;
import com.root.exception.NotFoundException;
import com.root.repository.ShowRepository;
import com.root.repository.TicketRepository;
import com.root.repository.UserRepository;
import com.root.resource.BookingResource;
import com.root.resource.TicketMessage;
import com.root.resource.TicketResource;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static String topic = "TICKET_BOOKED";

    ObjectMapper mapper = new ObjectMapper();

    public TicketResource bookTicket(BookingResource bookingResource) {
        Optional<User> optionalUser = userRepository.findById(bookingResource.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User Not Found with ID: " + bookingResource.getUserId() + " to book ticket");
        }

        Optional<Show> optionalShow = showRepository.findById(bookingResource.getShowId());
        if (optionalShow.isEmpty()) {
            throw new NotFoundException("Show Not Found with ID: " + bookingResource.getUserId() + " to book ticket");
        }

        Set<String> requestedSeats = bookingResource.getSeatsNumbers();

        List<ShowSeat> showSeatsEntities = optionalShow.get().getSeats();

        showSeatsEntities = showSeatsEntities.stream()
                .filter(seat -> seat.getSeatType().equals(bookingResource.getSeatType())
                        && !seat.isBooked()
                        && requestedSeats.contains(seat.getSeatNumber()))
                .collect(Collectors.toList());

        if (showSeatsEntities.size() != requestedSeats.size()) {
            throw new NotFoundException("Seats Not Available for Booking");
        }

        Ticket ticket = Ticket.builder()
                .user(optionalUser.get())
                .show(optionalShow.get())
                .seats(showSeatsEntities)
                .build();

        double amount = 0.0;

        String allotedSeats = "";

        for (ShowSeat seatsEntity : showSeatsEntities) {
            seatsEntity.setBooked(true);
            seatsEntity.setBookedAt(new Date());
            seatsEntity.setTicket(ticket);

            amount += seatsEntity.getRate();

            allotedSeats += seatsEntity.getSeatNumber() + " ";
        }

        ticket.setAmount(amount);
        ticket.setAllottedSeats(allotedSeats);

        if (CollectionUtils.isEmpty(optionalUser.get().getTicketEntities())) {
            optionalUser.get().setTicketEntities(new ArrayList<>());
        }

        optionalUser.get().getTicketEntities().add(ticket);

        if (CollectionUtils.isEmpty(optionalShow.get().getTickets())) {
            optionalShow.get().setTickets(new ArrayList<>());
        }
        optionalShow.get().getTickets().add(ticket);

        ticket = ticketRepository.save(ticket);

        // TODO Kafka messaging
        try {
            TicketMessage message = new TicketMessage(ticket.getUser().getName(),ticket.getUser().getMobile(), ticket.getUser().getEmail(), ticket.getShow(), ticket.getSeats());
            log.info("sending kafka message on booking {}", mapper.writeValueAsString(message));
            kafkaTemplate.send(topic,mapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Exception while sending notification service");
        }

        return Ticket.toResource(ticket);
    }


    public TicketResource getTicket(long id) {

        Optional<Ticket> ticketEntity = ticketRepository.findById(id);

        if (ticketEntity.isEmpty()) {
            log.error("Ticket not found for id: " + id);
            throw new EntityNotFoundException("Ticket Not Found with ID: " + id);
        }

        return Ticket.toResource(ticketEntity.get());
    }
}
