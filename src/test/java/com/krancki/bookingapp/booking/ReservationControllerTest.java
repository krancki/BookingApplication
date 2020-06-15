package com.krancki.bookingapp.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krancki.bookingapp.BaseIntegrationTest;
import com.krancki.bookingapp.booking.dto.CreateReservationDto;
import com.krancki.bookingapp.booking.dto.ReservationDto;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void shouldReturnTheUserReservations() throws Exception {
        //given
        String nickName = "Marks";
        List<Long> createdRoomIds = createTwoReservationByName(nickName);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/reservations/" + nickName));

        //then
        String responseContent = response.andReturn().getResponse().getContentAsString();
        response.andExpect(status().isOk());
        List<ReservationDto> reservationDto = List.of(objectMapper.readValue(responseContent, ReservationDto[].class));

        assertEquals(2, reservationDto.size());
        assertTrue(createdRoomIds.containsAll(
                reservationDto.stream()
                        .map(ReservationDto::getRoomId)
                        .collect(Collectors.toList())));

    }

    private List<Long> createTwoReservationByName(String nickName) throws Exception {
        List<Long> createdRoomIds = createRooms().stream().map(Room::getId).collect(Collectors.toList());

        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        createReservation(createdRoomIds.get(0), nickName, dateRange);
        createReservation(createdRoomIds.get(1), nickName, dateRange);
        return createdRoomIds;
    }

    @Test
    void shouldCreateReservation() throws Exception {
        //given
        Room createdRoom = createRooms().stream().findFirst().orElseThrow();

        CreateReservationDto createReservationDto =
                getCreateReservationDto(LocalDate.now(), LocalDate.now().plusDays(2), createdRoom);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        //then
        String responseContent = response.andReturn().getResponse().getContentAsString();
        response.andExpect(status().isCreated());
        ReservationDto reservationDto = objectMapper.readValue(responseContent, ReservationDto.class);

        assertEquals("Marks", reservationDto.getNickName());
        assertTrue(reservationDto.getBookingFrom().isEqual(LocalDate.now()));
        assertTrue(reservationDto.getBookingTo().isEqual(LocalDate.now().plusDays(2)));
        assertEquals(reservationDto.getRoomId(), createdRoom.getId());
    }

    @Test
    void shouldRemoveReservation() throws Exception {
        //given
        Room createdRoom = createRooms().get(0);

        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        ReservationDto createdReservationDto = createReservation(createdRoom.getId(), "Marks", dateRange);

        //when
        ResultActions response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/" + createdReservationDto.getId() + "/Marks"));

        //then
        String responseContent = response.andReturn().getResponse().getContentAsString();
        response.andExpect(status().isOk());
        ReservationDto reservationDto = objectMapper.readValue(responseContent, ReservationDto.class);

        assertEquals("Marks", reservationDto.getNickName());
        assertTrue(reservationDto.getBookingFrom().isEqual(LocalDate.now()));
        assertTrue(reservationDto.getBookingTo().isEqual(LocalDate.now().plusDays(2)));
        assertEquals(reservationDto.getRoomId(), createdRoom.getId());
    }

    @Test
    void shouldThrowExceptionWhenTryRemoveNotExistReservation() throws Exception {
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/100/Marks"));

        //then
        String responseContent = response.andReturn().getResponse().getErrorMessage();
        response.andExpect(status().isNotFound());

        assertEquals("Reservation not exist", responseContent);
    }


    @MethodSource("provideAllReservedDateRange")
    @ParameterizedTest
    void shouldThrowExceptionWhenRoomIsReserved(LocalDate localDateFrom, LocalDate localDateTo) throws Exception {
        //given
        Room createdRoom = createRooms().get(0);
        DateRange reservedDateRange = new DateRange(LocalDate.of(2000, 1, 10), LocalDate.of(2000, 1, 15));
        createReservation(createdRoom.getId(), "Parker", reservedDateRange);
        DateRange secondReservedDateRange = new DateRange(LocalDate.of(2000, 1, 20), LocalDate.of(2000, 1, 20));
        createReservation(createdRoom.getId(), "Parker", secondReservedDateRange);

        CreateReservationDto createReservationDto = getCreateReservationDto(localDateFrom, localDateTo, createdRoom);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        //then
        String responseContent = response.andReturn().getResponse().getErrorMessage();
        response.andExpect(status().isUnprocessableEntity());

        assertEquals("Room is not available", responseContent);
    }

    //2000-1-10 / 2000-1-15
    private static Stream<Arguments> provideAllReservedDateRange() {
        return Stream.of(
                Arguments.of(LocalDate.of(2000, 1, 8), LocalDate.of(2000, 1, 11)),
                Arguments.of(LocalDate.of(2000, 1, 9), LocalDate.of(2000, 1, 12)),
                Arguments.of(LocalDate.of(2000, 1, 10), LocalDate.of(2000, 1, 10)),
                Arguments.of(LocalDate.of(2000, 1, 10), LocalDate.of(2000, 1, 12)),
                Arguments.of(LocalDate.of(2000, 1, 11), LocalDate.of(2000, 1, 12)),
                Arguments.of(LocalDate.of(2000, 1, 14), LocalDate.of(2000, 1, 15)),
                Arguments.of(LocalDate.of(2000, 1, 10), LocalDate.of(2000, 1, 10)),
                Arguments.of(LocalDate.of(2000, 1, 8), LocalDate.of(2000, 1, 15)),
                Arguments.of(LocalDate.of(2000, 1, 8), LocalDate.of(2000, 1, 16)),
                Arguments.of(LocalDate.of(2000, 1, 20), LocalDate.of(2000, 1, 20))
        );
    }

    @MethodSource("provideAllFreeDateRange")
    @ParameterizedTest
    void shouldCreateReservationWhenRoomIsReservedForDifferentDateRange(LocalDate localDateFrom, LocalDate localDateTo) throws Exception {
        //given
        Room createdRoom = createRooms().get(0);
        DateRange reservedDateRange = new DateRange(LocalDate.of(2000, 1, 10), LocalDate.of(2000, 1, 15));
        createReservation(createdRoom.getId(), "Parker", reservedDateRange);

        CreateReservationDto createReservationDto = getCreateReservationDto(localDateFrom, localDateTo, createdRoom);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        //then
        response.andExpect(status().isCreated());
    }

    private CreateReservationDto getCreateReservationDto(LocalDate localDateFrom, LocalDate localDateTo,
                                                         Room createdRoom) {
        DateRange dateRange = new DateRange(localDateFrom, localDateTo);
        return new CreateReservationDto(createdRoom.getId(), "Marks", dateRange.getFrom(), dateRange.getTo());
    }

    //2000-1-10 / 2000-1-15
    private static Stream<Arguments> provideAllFreeDateRange() {
        return Stream.of(
                Arguments.of(LocalDate.of(2000, 1, 8), LocalDate.of(2000, 1, 10)),
                Arguments.of(LocalDate.of(2000, 1, 15), LocalDate.of(2000, 1, 15)),
                Arguments.of(LocalDate.of(2000, 1, 15), LocalDate.of(2000, 1, 17)),
                Arguments.of(LocalDate.of(2000, 1, 8), LocalDate.of(2000, 1, 9)),
                Arguments.of(LocalDate.of(2000, 1, 16), LocalDate.of(2000, 1, 18))
        );
    }

    private ReservationDto createReservation(Long roomId, String nickName, DateRange dateRange) throws Exception {

        CreateReservationDto createReservationDto =
                new CreateReservationDto(roomId, nickName, dateRange.getFrom(), dateRange.getTo());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        String responseContent = response.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, ReservationDto.class);
    }

    @Value
    private static class DateRange {
        LocalDate from;
        LocalDate to;

        public DateRange(LocalDate from, LocalDate to) {
            this.from = from;
            this.to = to;
        }
    }

    private List<Room> createRooms() {
        return List.of(
                roomRepository.save(new Room((short) 4, BigDecimal.valueOf(100L), "Beautiful room")),
                roomRepository.save(new Room((short) 2, BigDecimal.valueOf(50L), "Small room"))
        );
    }
}