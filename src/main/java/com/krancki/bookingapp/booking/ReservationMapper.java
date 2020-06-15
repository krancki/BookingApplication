package com.krancki.bookingapp.booking;

import com.krancki.bookingapp.booking.dto.ReservationDto;

class ReservationMapper {

    static ReservationDto mapReservationToReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .nickName(reservation.getNickName())
                .bookingFrom(reservation.getBookingFrom())
                .bookingTo(reservation.getBookingTo())
                .roomId(reservation.getRoomId())
                .build();
    }
}
