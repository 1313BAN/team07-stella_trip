package com.ssafy.stella_trip.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private int roomId;
    private int sidoCode;
    private String sidoName;
    private int gugunCode;
    private String gugunName;
}
