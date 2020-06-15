package com.krancki.bookingapp.notification.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public
class NotificationDto {
    Long id;
    String nickName;
    String message;
    LocalDateTime createdTime;
}
