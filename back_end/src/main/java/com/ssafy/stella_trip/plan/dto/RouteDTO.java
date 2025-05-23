package com.ssafy.stella_trip.plan.dto;

import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    private int routeId;
    private int planId;
    private int attractionId;
    private AttractionDTO attraction;
    private int dayIndex;
    private int order;
    private LocalTime visitTime;
    private String memo;
}
