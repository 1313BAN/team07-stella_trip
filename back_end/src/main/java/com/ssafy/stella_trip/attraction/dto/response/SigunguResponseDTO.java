package com.ssafy.stella_trip.attraction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigunguResponseDTO {
    List<SidoResponseDTO> sidoList;
}
