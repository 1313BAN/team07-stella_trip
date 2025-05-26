package com.ssafy.stella_trip.stella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StellaListResponseDTO {
    private List<StellaResponseDTO> stellaList;
}
