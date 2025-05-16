package com.ssafy.stella_trip.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikedReviewDTO {
    private int userId;
    private int attractionId;
    private int reviewId;
}