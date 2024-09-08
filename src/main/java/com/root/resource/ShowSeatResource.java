package com.root.resource;

import com.root.enums.SeatType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ShowSeatResource {

    private long id;

    private String seatNumber;

    private int rate;

    private SeatType seatType;

    private boolean booked;

    private Date bookedAt;
}
