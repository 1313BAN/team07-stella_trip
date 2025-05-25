package com.ssafy.stella_trip.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private int notificationId;
    private int userId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
