package com.ssafy.stella_trip.plan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanScheduleRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
