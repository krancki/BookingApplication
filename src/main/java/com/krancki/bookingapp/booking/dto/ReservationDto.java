package com.krancki.bookingapp.booking.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class ReservationDto {
    Long id;
    String nickName;
    LocalDate bookingFrom;
    LocalDate bookingTo;

    Long roomId;
}
