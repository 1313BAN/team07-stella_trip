package com.ssafy.stella_trip.chat.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChatListResponseDTO {
    int size;
    List<ChatResponseDTO> chats;
}
