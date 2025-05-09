package com.ssafy.stella_trip.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Builder
public class TagResponseDTO {
    @JsonProperty("tag_id")
    private int tagId;
    private String name;
}
