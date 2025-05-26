package com.ssafy.stella_trip.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class GPTResponseDTO {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private GPTMessageDTO message;
    }
}
