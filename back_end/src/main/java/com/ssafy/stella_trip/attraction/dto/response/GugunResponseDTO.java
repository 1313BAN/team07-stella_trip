package com.ssafy.stella_trip.attraction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GugunResponseDTO {
    private int sidoCode;
    private int gugunCode;
    private String gugunName;
}
