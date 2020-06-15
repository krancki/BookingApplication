package com.krancki.bookingapp.notification;

import com.krancki.bookingapp.booking.event.UserCreateReservationEvent;
import com.krancki.bookingapp.time.provider.TimeProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TimeProvider timeProvider;

    @EventListener
    void createNotification(UserCreateReservationEvent userCreateReservationEvent) {
        notificationRepository.save(Notification.builder()
                .createdTime(timeProvider.nowAsDateTime())
                .nickName(userCreateReservationEvent.getNickName())
                .message(userCreateReservationEvent.getDescription())
                .readByUser(false)
                .notificationDeliveryDate(userCreateReservationEvent.getNotificationDeliveryDate())
                .build());
    }

    List<Notification> getAllNotification(String nickName) {
        List<Notification> notificationList = notificationRepository.findByNickNameOrderByCreatedTimeAsc(nickName);
        setNotificationsAsReadByUser(notificationList);
        return notificationList;
    }

    List<Notification> getNewNotification(String nickName) {
        List<Notification> notificationList = notificationRepository.findNewByNickName(nickName, false, timeProvider.nowAsLocalDate());
        setNotificationsAsReadByUser(notificationList);
        return notificationList;
    }

    private void setNotificationsAsReadByUser(List<Notification> notificationList) {
        notificationRepository.saveAll(notificationList.stream().peek(Notification::setAsReadByUser).collect(Collectors.toList()));
    }

}