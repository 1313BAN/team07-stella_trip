package com.ssafy.stella_trip.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanResponseDTO {
    private int planId;
    private String title;
    private String description;
    private String stella;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    private int likeCount;
    private boolean isPublic;
    @JsonProperty("plan_writers")
    private List<WriterResponseDTO> planWriters;
    private List<TagResponseDTO> tags;
}
