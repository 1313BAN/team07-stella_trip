package com.ssafy.stella_trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
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
    private LocalDate visitDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isLiked;
}
