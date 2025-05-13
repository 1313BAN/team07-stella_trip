package com.ssafy.stella_trip.notification.controller;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.notification.dto.response.NotificationResponseDTO;
import com.ssafy.stella_trip.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public PageDTO<NotificationResponseDTO> getNotifications(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        return notificationService.getNotificationList(page, size);
    }

    @GetMapping("/{notificationId}")
    public NotificationResponseDTO getNotificationDetail(@PathVariable(value = "notificationId") int notificationId) {
        return notificationService.getNotificationDetail(notificationId);
    }
}
