package com.krancki.bookingapp.notification;

import com.krancki.bookingapp.notification.dto.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("{nickName}")
    List<NotificationDto> getNotReadUserNotification(@PathVariable String nickName) {
        return notificationService.getNewNotification(nickName).stream()
                .map(NotificationMapper::mapNotificationToNotificationDto).collect(
                        Collectors.toList());
    }

    @GetMapping("{nickName}/all")
    List<NotificationDto> getAllUserNotification(@PathVariable String nickName) {
        return notificationService.getAllNotification(nickName).stream()
                .map(NotificationMapper::mapNotificationToNotificationDto).collect(
                        Collectors.toList());
    }
}
