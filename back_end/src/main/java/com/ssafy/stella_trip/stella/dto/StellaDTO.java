package com.ssafy.stella_trip.stella.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StellaDTO {
    private int stellaLinkId;
    private int planId;
    private String stellaData;
    private String stellaLink;
    private String stellaAI;
}
