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
    private int reviewId;
    private int userId;
    private String userName;
    private String title;
    private String content;
    private double rating;
    private LocalDate visitDate;
    private LocalDateTime createdAt;
    private int likeCount;
    @JsonProperty("isLiked")
    private boolean isLiked;
}