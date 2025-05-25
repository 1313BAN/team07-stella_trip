package com.ssafy.stella_trip.attraction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDTO {
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
}
