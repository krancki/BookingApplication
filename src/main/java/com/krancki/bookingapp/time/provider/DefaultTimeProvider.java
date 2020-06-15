package com.krancki.bookingapp.time.provider;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DefaultTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime nowAsDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate nowAsLocalDate() {
        return LocalDate.now();
    }
}
