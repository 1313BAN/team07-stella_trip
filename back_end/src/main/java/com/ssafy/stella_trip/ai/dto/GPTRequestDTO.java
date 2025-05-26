package com.ssafy.stella_trip.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GPTRequestDTO {
    private String model;
    private List<GPTMessageDTO> messages;
}
