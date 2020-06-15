package com.krancki.bookingapp.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByIdAndNickName(long reservationId, String nickName);

    List<Reservation> findByNickName(String nickName);
}