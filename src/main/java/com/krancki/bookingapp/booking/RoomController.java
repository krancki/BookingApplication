package com.krancki.bookingapp.booking;

import com.krancki.bookingapp.booking.dto.RoomDto;
import com.krancki.bookingapp.booking.vo.ReservationDateVO;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
class RoomController {

    private final BookingService bookingService;

    @GetMapping
    ResponseEntity<List<RoomDto>> getFreeRooms(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate from,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate to) {
        return ResponseEntity.ok(
                bookingService.getFreeRooms(
                        ReservationDateVO.builder()
                                .from(from)
                                .to(to)
                                .build())
                        .stream()
                        .map(RoomMapper::mapRoomToRoomDto)
                        .collect(Collectors.toList()));
    }
}