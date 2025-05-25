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
    private int planId;
    private String title;
    private String description;
    @JsonRawValue
    private String stella;
    private LocalDate startDate;
    private LocalDate endDate;
    private int likeCount;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private List<WriterResponseDTO> planWriters;
    private List<TagResponseDTO> tags;
    @JsonProperty("isLiked")
    private boolean liked;
}
