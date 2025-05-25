package com.ssafy.stella_trip.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponseDTO {
    private int roomId;
    private int sidoCode;
    private String sidoName;
    private int gugunCode;
    private String gugunName;
}
