package com.krancki.bookingapp.booking;

import com.krancki.bookingapp.booking.dto.CreateRoomDto;
import com.krancki.bookingapp.booking.dto.RoomDto;
import com.krancki.bookingapp.booking.vo.ReservationDateVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class BookingFacade {

    private final RoomRepository roomRepository;
    private final BookingService bookingService;

    public RoomDto createRoom(CreateRoomDto roomDto) {
        return RoomMapper.mapRoomToRoomDto(roomRepository.save(RoomMapper.mapCreateRoomDtoToRoom(roomDto)));
    }

    public void createReservation(Long id, String nickName, LocalDate from, LocalDate to) {
        bookingService.createReservation(id,
                nickName,
                ReservationDateVO.builder()
                        .from(from)
                        .to(to)
                        .build());
    }
}
