package com.krancki.bookingapp.booking.vo;

import com.krancki.bookingapp.booking.exception.InvalidDateException;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class ReservationDateVO {
    LocalDate from;
    LocalDate to;

    private ReservationDateVO(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new InvalidDateException(from, to);
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return from + " - " + to;
    }
}