package com.krancki.bookingapp.notification;

import com.krancki.bookingapp.notification.dto.NotificationDto;

class NotificationMapper {

    static NotificationDto mapNotificationToNotificationDto(Notification notification) {
        return new NotificationDto(notification.getId(),
                notification.getNickName(),
                notification.getMessage(),
                notification.getCreatedTime());
    }
}
