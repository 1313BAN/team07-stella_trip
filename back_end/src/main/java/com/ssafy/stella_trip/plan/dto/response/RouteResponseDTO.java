package com.ssafy.stella_trip.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponseDTO {
    private int routeId;
    private int attractionId;
    private int order;
    private String name;
    private String image;
    private String address;
    private int contentTypeId;
    private int likeCount;
    private double rating;
    private double latitude;
    private double longitude;
    private LocalTime visitTime;
    private String memo;
}
