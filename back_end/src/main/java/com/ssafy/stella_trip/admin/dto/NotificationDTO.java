package com.ssafy.stella_trip.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private int notificationId;
    private int userId;
    private String title;
    private String content;
    private LocalDateTime creatdAt;
    private LocalDateTime updatdAt;
}
