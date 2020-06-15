package com.krancki.bookingapp.booking.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class CreateRoomDto {
    short roomSize;
    BigDecimal price;
    String description;
}
