package com.krancki.bookingapp.booking.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class RoomDto {
    Long id;
    short size;
    BigDecimal price;
    String description;
}
