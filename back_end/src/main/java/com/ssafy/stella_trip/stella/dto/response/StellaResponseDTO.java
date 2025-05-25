package com.ssafy.stella_trip.stella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StellaResponseDTO {
    private int planId;
    private String stellaData;
    private String stellaLink;
}
