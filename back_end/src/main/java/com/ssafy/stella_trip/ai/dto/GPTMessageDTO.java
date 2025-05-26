package com.ssafy.stella_trip.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GPTMessageDTO {
    private String role;     // system, user, assistant
    private String content;
}
