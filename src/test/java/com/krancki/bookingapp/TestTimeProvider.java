package com.krancki.bookingapp;

import com.krancki.bookingapp.time.provider.TimeProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
@Primary
public class TestTimeProvider implements TimeProvider {

    private Clock currentClock = Clock.systemDefaultZone();
    private final ZoneId systemZone = ZoneId.systemDefault();

    public void setDefaultClock() {
        currentClock = Clock.systemDefaultZone();
    }

    public void setFixedClock(LocalDateTime dateTime) {
        currentClock = Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), systemZone);
    }

    @Override
    public LocalDateTime nowAsDateTime() {
        return LocalDateTime.now(currentClock);
    }

    @Override
    public LocalDate nowAsLocalDate() {
        return LocalDate.now(currentClock);
    }
}
