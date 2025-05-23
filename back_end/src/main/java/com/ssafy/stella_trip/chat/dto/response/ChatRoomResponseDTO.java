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
    @JsonProperty("room_id")
    private int roomId;
    @JsonProperty("sido_code")
    private int sidoCode;
    @JsonProperty("sido_name")
    private String sidoName;
    @JsonProperty("gugun_code")
    private int gugunCode;
    @JsonProperty("gugun_name")
    private String gugunName;
}
