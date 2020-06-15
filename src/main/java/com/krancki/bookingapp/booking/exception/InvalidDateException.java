package com.krancki.bookingapp.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

public class InvalidDateException extends ResponseStatusException {
    public InvalidDateException(LocalDate from, LocalDate to) {
        super(HttpStatus.BAD_REQUEST, from + " cant be after " + to, new RuntimeException());
    }
}
