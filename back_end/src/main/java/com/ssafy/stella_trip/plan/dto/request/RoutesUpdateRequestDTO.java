package com.ssafy.stella_trip.plan.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutesUpdateRequestDTO {
    List<RouteDTO> routes;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteDTO{
        private int routeId;
        private int dayIndex;
        private int order;
        private boolean deleted;
    }
}
