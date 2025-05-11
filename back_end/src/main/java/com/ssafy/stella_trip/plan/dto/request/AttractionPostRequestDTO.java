package com.ssafy.stella_trip.plan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionPostRequestDTO {
    @JsonProperty("day_index")
    private int dayIndex;
    @JsonProperty("attraction_id")
    private int attractionId;
    @JsonProperty("visit_time")
    private LocalTime visitTime;
    private String memo;
}
