package com.krancki.bookingapp.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReservationNotExistException extends ResponseStatusException {
    public ReservationNotExistException() {
        super(HttpStatus.NOT_FOUND, "Reservation not exist");
    }
}
