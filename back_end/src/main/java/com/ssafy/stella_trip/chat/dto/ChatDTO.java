package com.ssafy.stella_trip.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private int chatId;
    private int roomId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
}
