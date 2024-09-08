package com.root.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.root.enums.SeatType;
import com.root.resource.ShowSeatResource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "show_seats")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "rate", nullable = false)
    private int rate;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    @Column(name = "is_booked", columnDefinition = "bit(1) default 0", nullable = false)
    private boolean booked;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "booked_at")
    @CreationTimestamp
    private Date bookedAt;

    @ManyToOne
    @JoinColumn(name = "show_id")
    @JsonIgnore
    private Show show;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public static List<ShowSeatResource> toResource(List<ShowSeat> seats) {
        if (!CollectionUtils.isEmpty(seats)) {
            return seats.stream().map(ShowSeat::toResource).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static ShowSeatResource toResource(ShowSeat seatsEntity) {
        return ShowSeatResource.builder()
                .id(seatsEntity.getId())
                .seatNumber(seatsEntity.getSeatNumber())
                .rate(seatsEntity.getRate())
                .seatType(seatsEntity.getSeatType())
                .booked(seatsEntity.isBooked())
                .bookedAt(seatsEntity.getBookedAt())
                .build();
    }
}
