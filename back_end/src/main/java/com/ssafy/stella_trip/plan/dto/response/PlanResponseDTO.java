package com.ssafy.stella_trip.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PlanResponseDTO {
    @JsonProperty("plan_id")
    private int planId;
    private String title;
    private String description;
    @JsonRawValue
    private String stella;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("is_public")
    private boolean isPublic;
    @JsonProperty("plan_writers")
    private List<WriterResponseDTO> planWriters;
    private List<TagResponseDTO> tags;
    private boolean liked;
}
