package com.krancki.bookingapp.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT ro FROM Room ro " +
            "WHERE ro.id NOT IN " +
            "(SELECT rox.id from Room rox " +
            "JOIN Reservation AS res ON rox.id = res.roomId " +
            "WHERE (:from <= res.bookingFrom AND :to > res.bookingFrom ) " +
            "OR  (:from >= res.bookingFrom AND :to < res.bookingTo ) " +
            "OR  (:from < res.bookingTo AND :from >= res.bookingFrom ) " +
            "OR (:from = res.bookingFrom))"
    )
    List<Room> findFreeRoomsByDate(LocalDate from, LocalDate to);

    @Query("SELECT ro FROM Room ro " +
            "WHERE ro.id = :roomId AND ro.id NOT IN " +
            "(SELECT rox.id from Room rox " +
            "JOIN Reservation AS res ON rox.id = res.roomId " +
            "WHERE res.roomId = :roomId " +
            "AND (:from <= res.bookingFrom AND :to > res.bookingFrom ) " +
            "OR  (:from >= res.bookingFrom AND :to < res.bookingTo ) " +
            "OR  (:from < res.bookingTo AND :from >= res.bookingFrom ) " +
            "OR (:from = res.bookingFrom))"
    )
    Optional<Room> findFreeRoomByDate(long roomId, LocalDate from, LocalDate to);
}