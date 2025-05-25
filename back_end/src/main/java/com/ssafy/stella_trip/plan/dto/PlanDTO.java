package com.ssafy.stella_trip.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.stella_trip.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDTO {
    private int planId;
    private List<UserDTO> writers;
    private List<RouteDTO> routes;
    private List<TagDTO> tags;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private String stella;
    @JsonProperty("isLiked")
    private boolean isLiked;
}
