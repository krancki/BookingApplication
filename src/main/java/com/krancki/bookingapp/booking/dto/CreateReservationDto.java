package com.krancki.bookingapp.booking.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Value
public class CreateReservationDto {
    @Positive
    Long roomId;
    @NotBlank
    String nickName;
    @NotNull
    LocalDate from;
    @NotNull
    LocalDate to;
}