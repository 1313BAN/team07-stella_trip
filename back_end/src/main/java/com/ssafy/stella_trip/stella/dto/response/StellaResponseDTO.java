package com.ssafy.stella_trip.stella.dto.response;

import com.ssafy.stella_trip.ai.dto.TarotResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StellaResponseDTO {
    private int planId;
    private int userId;
    private String stellaData;
    private String stellaLink;
    private TarotResult stellaAI;
}
