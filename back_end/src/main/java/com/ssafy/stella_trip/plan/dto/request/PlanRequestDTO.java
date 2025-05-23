package com.ssafy.stella_trip.plan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequestDTO {
    private String title;
    private String description;
    private List<String> tags;
    private boolean isPublic;
    private LocalDate startDate;
    private LocalDate endDate;
}
