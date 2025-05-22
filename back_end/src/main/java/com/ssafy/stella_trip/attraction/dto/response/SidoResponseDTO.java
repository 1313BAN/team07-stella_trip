package com.ssafy.stella_trip.attraction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SidoResponseDTO {
    private int sidoCode;
    private String sidoName;
    private List<GugunResponseDTO> gugunList;
}
