package com.krancki.bookingapp.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krancki.bookingapp.BaseIntegrationTest;
import com.krancki.bookingapp.TestTimeProvider;
import com.krancki.bookingapp.booking.dto.CreateReservationDto;
import com.krancki.bookingapp.booking.dto.RoomDto;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoomControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    TestTimeProvider testTimeProvider;

    @BeforeEach
    void setUp() {
        testTimeProvider.setFixedClock(LocalDateTime.of(2000, 1, 1, 10, 10, 0));
    }

    @Test
    void shouldReturnAvailableRooms() throws Exception {
        //given
        createRooms();

        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rooms")
                .param("from", getFormattedLocalDate(dateRange.getFrom()))
                .param("to", getFormattedLocalDate(dateRange.getTo())));

        //then
        String responseContent = response.andReturn().getResponse().getContentAsString();
        response.andExpect(status().isOk());
        List<RoomDto> responseDto = List.of(objectMapper.readValue(responseContent, RoomDto[].class));


        assertEquals(2, responseDto.size());
        assertTrue(responseDto.stream().anyMatch(roomDto -> roomDto.getDescription().equals("Beautiful room")
                && roomDto.getPrice().longValue() == 100L
                && roomDto.getSize() == 4
        ));
        assertTrue(responseDto.stream().anyMatch(roomDto -> roomDto.getDescription().equals("Small room")
                && roomDto.getPrice().longValue() == 50L
                && roomDto.getSize() == 2
        ));
    }


    @Test
    void shouldNotReturnReservedRoom() throws Exception {
        //given
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        Room createdRoom = createRooms().stream().findFirst().orElseThrow();
        createReservation(createdRoom, dateRange);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rooms")
                .param("from", getFormattedLocalDate(dateRange.getFrom()))
                .param("to", getFormattedLocalDate(dateRange.getTo())));

        //then
        String responseContent = response.andReturn().getResponse().getContentAsString();
        response.andExpect(status().isOk());
        List<RoomDto> responseDto = List.of(objectMapper.readValue(responseContent, RoomDto[].class));

        assertFalse(responseDto.stream().anyMatch(roomDto -> roomDto.getId().equals(createdRoom.getId())));
    }

    @Test
    void shouldThrowExceptionWhenDateRangeIsIncorrect() throws Exception {
        //given
        DateRange dateRange =
                new DateRange(testTimeProvider.nowAsLocalDate().plusDays(2), testTimeProvider.nowAsLocalDate());

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rooms")
                .param("from", getFormattedLocalDate(dateRange.getFrom()))
                .param("to", getFormattedLocalDate(dateRange.getTo())));

        //then
        String responseContent = response.andReturn().getResponse().getErrorMessage();
        response.andExpect(status().isBadRequest());

        assertEquals("2000-01-03 cant be after 2000-01-01", responseContent);
    }

    private String getFormattedLocalDate(LocalDate to) {
        return to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private void createReservation(Room room, DateRange dateRange) throws Exception {

        CreateReservationDto createReservationDto =
                new CreateReservationDto(room.getId(), "Nowak", dateRange.getFrom(), dateRange.getTo());
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));
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