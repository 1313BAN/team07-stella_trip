package com.ssafy.stella_trip.attraction.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("user_name")
    private String userName;
    private String title;
    private String content;
    private double rating;
    @JsonProperty("visit_date")
    private LocalDate visitDate;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private boolean isLiked;
}