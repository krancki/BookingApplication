package com.krancki.bookingapp;

import com.krancki.bookingapp.booking.BookingFacade;
import com.krancki.bookingapp.booking.dto.CreateRoomDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
@Component
@ConditionalOnProperty(name = "app.db-dump-init", havingValue = "true")
class DbDumpInitializer implements CommandLineRunner {

    private final BookingFacade bookingFacade;

    @Override
    public void run(String... args) throws Exception {
        try {
            createRooms();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createRooms() {
        bookingFacade.createRoom(new CreateRoomDto((short) 4, BigDecimal.valueOf(100L), "Beatiful room"));
        bookingFacade.createRoom(new CreateRoomDto((short) 6, BigDecimal.valueOf(200L), "Big room"));
        bookingFacade.createRoom(new CreateRoomDto((short) 2, BigDecimal.valueOf(300L), "Expensive room"));
        bookingFacade.createRoom(new CreateRoomDto((short) 2, BigDecimal.valueOf(59L), "Small room"));
    }

}
