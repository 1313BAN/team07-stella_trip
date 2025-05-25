package com.ssafy.stella_trip.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoomListResponseDTO {
    List<Sido> sidoList;

    @Data
    @AllArgsConstructor
    public static class Sido {
        private int sidoCode;
        private String sidoName;
        List<ChatRoomResponseDTO> rooms;
    }
}
