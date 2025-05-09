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
    @JsonProperty("route_id")
    private int routeId;
    @JsonProperty("attraction_id")
    private int attractionId;
    private int order;
    private String name;
    private String image;
    private String address;
    @JsonProperty("content_type_id")
    private int contentTypeId;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("rating")
    private double rating;
    private double latitude;
    private double longitude;
    @JsonProperty("visit_time")
    private LocalTime visitTime;
    private String memo;
}
