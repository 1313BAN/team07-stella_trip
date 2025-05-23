package com.ssafy.stella_trip.plan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteUpdateRequestDTO {
    private LocalTime visitTime;
    private String memo;
}
