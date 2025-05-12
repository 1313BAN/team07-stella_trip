package com.ssafy.stella_trip.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PlanDetailResponseDTO extends PlanResponseDTO{
    private Map<LocalDate, List<RouteResponseDTO>> details;
}
