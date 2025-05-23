package com.ssafy.stella_trip.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoomListResponseDTO {
    @JsonProperty("sido_list")
    List<Sido> sidoList;

    @Data
    @AllArgsConstructor
    public static class Sido {
        @JsonProperty("sido_code")
        private int sidoCode;
        @JsonProperty("sido_name")
        private String sidoName;
        List<ChatRoomResponseDTO> rooms;
    }
}
