package com.krancki.bookingapp.booking;

import com.krancki.bookingapp.booking.dto.CreateReservationDto;
import com.krancki.bookingapp.booking.dto.ReservationDto;
import com.krancki.bookingapp.booking.vo.ReservationDateVO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
class ReservationController {

    private final BookingService bookingService;

    @PostMapping
    ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody CreateReservationDto createReservationDto) {
        return ResponseEntity.status(201)
                .body(ReservationMapper.mapReservationToReservationDto(
                        bookingService.createReservation(
                                createReservationDto.getRoomId(),
                                createReservationDto.getNickName(),
                                ReservationDateVO.builder()
                                        .from(createReservationDto.getFrom())
                                        .to(createReservationDto.getTo())
                                        .build()
                        )));
    }

    @DeleteMapping("/{reservationId}/{nickName}")
    ResponseEntity<ReservationDto> deleteReservation(@PathVariable long reservationId, @PathVariable String nickName) {
        return ResponseEntity.ok(ReservationMapper
                .mapReservationToReservationDto(bookingService.removeReservation(reservationId, nickName)));

    }

    @GetMapping("/{nickName}")
    ResponseEntity<List<ReservationDto>> getReservation(@PathVariable String nickName) {
        return ResponseEntity.ok(
                bookingService.getUserReservation(nickName)
                        .stream()
                        .map(ReservationMapper::mapReservationToReservationDto)
                        .collect(Collectors.toList()));
    }

}
