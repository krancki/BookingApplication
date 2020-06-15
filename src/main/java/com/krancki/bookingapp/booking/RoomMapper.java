package com.krancki.bookingapp.booking;

import com.krancki.bookingapp.booking.dto.CreateRoomDto;
import com.krancki.bookingapp.booking.dto.RoomDto;

class RoomMapper {

    static Room mapCreateRoomDtoToRoom(CreateRoomDto roomDto) {
        return Room.builder()
                .size(roomDto.getRoomSize())
                .price(roomDto.getPrice())
                .description(roomDto.getDescription())
                .build();

    }

    static RoomDto mapRoomToRoomDto(Room room) {
        return new RoomDto(room.getId(), room.getSize(), room.getPrice(), room.getDescription());
    }
}