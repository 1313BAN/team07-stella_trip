package com.ssafy.stella_trip.stella.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StellaRequestDTO {
    private int planId; // 여행 계획 ID
    private String stellaData; // Stella 데이터 (svg 형식)
}
