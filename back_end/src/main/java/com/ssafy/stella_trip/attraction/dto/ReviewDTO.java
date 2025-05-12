package com.ssafy.stella_trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewId;
    private int attractionId;
    private int userId;
    private String content;
    private String title;
    private double rating;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
