package com.krancki.bookingapp.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krancki.bookingapp.BaseIntegrationTest;
import com.krancki.bookingapp.TestTimeProvider;
import com.krancki.bookingapp.booking.BookingFacade;
import com.krancki.bookingapp.booking.dto.CreateRoomDto;
import com.krancki.bookingapp.booking.dto.RoomDto;
import com.krancki.bookingapp.notification.dto.NotificationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest extends BaseIntegrationTest {

    @Autowired
    private BookingFacade bookingFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TestTimeProvider testTimeProvider;

    @Test
    void shouldReturnNewNotification() throws Exception {
        //given
        String nickName = "Marks";
        createRoomAndReservationToGetNewNotification(nickName);
        testTimeProvider.setFixedClock(getDateAfterNotificationDeliveryDate());

        //when
        ResultActions response = getNewUserNotifications(nickName);

        //then
        List<NotificationDto> notifications = getNewNotificationListFromResponse(response);

        assertEquals(1, notifications.size());
        assertTrue(notifications.stream().anyMatch(notificationDto ->
                notificationDto.getMessage()
                        .equals("We remind you about the reservation:Small room")));
    }

    @Test
    void shouldNotReturnNewNotificationWhenIsBeforeDeliveryDate() throws Exception {
        //given
        String nickName = "Marks";
        createRoomAndReservationToGetNewNotification(nickName);
        testTimeProvider.setFixedClock(
                getDateBeforeNotificationDeliverDate()
        );

        //when
        ResultActions response = getNewUserNotifications(nickName);

        //then
        List<NotificationDto> notifications = getNewNotificationListFromResponse(response);

        assertEquals(0, notifications.size());
    }

    @Test
    void shouldNotReturnNewNotificationWhenNotificationIsMarkAsRead() throws Exception {
        //given
        String nickName = "Marks";
        createRoomAndReservationToGetNewNotification(nickName);
        testTimeProvider.setFixedClock(
                getDateAfterNotificationDeliveryDate()
        );
        getNewUserNotifications(nickName);

        //when
        ResultActions response = getNewUserNotifications(nickName);

        //then
        List<NotificationDto> notifications = getNewNotificationListFromResponse(response);

        assertEquals(0, notifications.size());
    }

    private List<NotificationDto> getNewNotificationListFromResponse(ResultActions response) throws Exception {
        String responseContent = response.andReturn().getResponse().getContentAsString();
        response.andExpect(status().isOk());
        return List.of(objectMapper.readValue(responseContent, NotificationDto[].class));
    }

    private ResultActions getNewUserNotifications(String nickName) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/notifications/" + nickName));
    }

    private LocalDateTime getDateAfterNotificationDeliveryDate() {
        return LocalDateTime.of(2000, 1, 9, 0, 0, 0);
    }

    private LocalDateTime getDateBeforeNotificationDeliverDate() {
        return LocalDateTime.of(2000, 1, 8, 22, 59, 59);
    }

    private void createRoomAndReservationToGetNewNotification(String nickName) {
        RoomDto createdRoom = bookingFacade.createRoom(
                new CreateRoomDto((short) 4, new BigDecimal(100L), "Small room"));
        bookingFacade.createReservation(createdRoom.getId(),
                nickName,
                LocalDate.of(2000, 1, 10),
                LocalDate.of(2000, 1, 12));
    }

}