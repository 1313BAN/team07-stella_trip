package com.ssafy.stella_trip.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagResponseDTO {
    @JsonProperty("tag_id")
    private int tagId;
    private String name;
}
