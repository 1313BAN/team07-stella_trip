package com.ssafy.stella_trip.attraction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.stella_trip.attraction.dto.response.ReviewResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttractionWithReviewsDTO {
    private int attractionId;
    private int sidoCode;
    private int gugunCode;
    private int contentTypeId;
    private int contentId;
    private String title;
    private String firstImage1;
    private String firstImage2;
    private int mapLevel;
    private double latitude;
    private double longitude;
    private String tel;
    private String addr1;
    private String addr2;
    private String homepage;
    private String overview;
    private double rating;
    private int likeCount;
    @JsonProperty("isLiked")
    private boolean isLiked;

    private List<ReviewResponseDTO> reviews;
}