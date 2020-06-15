package com.krancki.bookingapp.booking.vo;

import com.krancki.bookingapp.booking.exception.InvalidDateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationDateVOTest {

    @Test
    void shouldCreateReservationDateVo() {
        //Given
        LocalDate from = LocalDate.of(2000, 1, 10);
        LocalDate to = LocalDate.of(2000, 1, 10);

        //When
        ReservationDateVO reservationDateVO =
                ReservationDateVO.builder()
                        .from(from)
                        .to(to)
                        .build();

        //Then
        assertTrue(reservationDateVO.getFrom().isEqual(LocalDate.of(2000, 1, 10)));
        assertTrue(reservationDateVO.getTo().isEqual(LocalDate.of(2000, 1, 10)));
    }


    @Test
    void shouldThrowExceptionWhenTryCreateIncorrectReservationDateVo() {
        //Given
        LocalDate from = LocalDate.of(2000, 1, 10);
        LocalDate to = LocalDate.of(2000, 1, 9);

        //Then
        assertThrows(InvalidDateException.class, () ->
                ReservationDateVO.builder()
                        .from(from)
                        .to(to)
                        .build());
    }


}