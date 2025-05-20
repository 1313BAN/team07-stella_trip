package com.ssafy.stella_trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigunguDTO {
    private int sidoCode;
    private String sidoName;
    private int gugunCode;
    private String gugunName;
}
