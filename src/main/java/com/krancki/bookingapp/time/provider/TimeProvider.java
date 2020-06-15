package com.krancki.bookingapp.time.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime nowAsDateTime();

    LocalDate nowAsLocalDate();
}
