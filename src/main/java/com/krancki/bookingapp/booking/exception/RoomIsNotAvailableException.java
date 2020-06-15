package com.krancki.bookingapp.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoomIsNotAvailableException extends ResponseStatusException {
    public RoomIsNotAvailableException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Room is not available");
    }
}
