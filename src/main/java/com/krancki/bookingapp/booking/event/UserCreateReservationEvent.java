package com.krancki.bookingapp.booking.event;

import lombok.Value;

import java.time.LocalDate;

@Value
public class UserCreateReservationEvent {

    String nickName;
    String description;
    String reservationDate;
    LocalDate notificationDeliveryDate;

}
