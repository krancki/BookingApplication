package com.krancki.bookingapp.booking;

import com.krancki.bookingapp.booking.event.UserCreateReservationEvent;
import com.krancki.bookingapp.booking.exception.ReservationNotExistException;
import com.krancki.bookingapp.booking.exception.RoomIsNotAvailableException;
import com.krancki.bookingapp.booking.vo.ReservationDateVO;
import com.krancki.bookingapp.time.provider.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
class BookingService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final TimeProvider timeProvider;
    private final ApplicationEventPublisher publisher;

    List<Room> getFreeRooms(ReservationDateVO reservationDate) {
        return roomRepository.findFreeRoomsByDate(reservationDate.getFrom(), reservationDate.getTo());
    }

    Reservation createReservation(long roomId, String nickName, ReservationDateVO reservationDate) {
        Room room = roomRepository.findFreeRoomByDate(roomId, reservationDate.getFrom(), reservationDate.getTo())
                .orElseThrow(RoomIsNotAvailableException::new);

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .nickName(nickName)
                        .bookingFrom(reservationDate.getFrom())
                        .bookingTo(reservationDate.getTo())
                        .createdTime(timeProvider.nowAsDateTime())
                        .roomId(room.getId())
                        .build());

        publishEvent(nickName, reservationDate, room);
        return reservation;
    }

    private void publishEvent(String nickName, ReservationDateVO reservationDate, Room room) {
        publisher.publishEvent(
                new UserCreateReservationEvent(nickName, "We remind you about the reservation:" + room.getDescription(),
                        reservationDate.toString(), reservationDate.getFrom().minusDays(1)));
    }

    Reservation removeReservation(long reservationId, String nickName) {
        Reservation reservation = reservationRepository.findByIdAndNickName(reservationId, nickName)
                .orElseThrow(ReservationNotExistException::new);
        reservationRepository.delete(reservation);
        return reservation;
    }

    List<Reservation> getUserReservation(String nickName) {
        return reservationRepository.findByNickName(nickName);
    }
}
