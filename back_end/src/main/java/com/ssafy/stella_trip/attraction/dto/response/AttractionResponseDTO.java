package com.ssafy.stella_trip.attraction.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttractionResponseDTO {
    private int attractionId;
    private String name;
    private String image;
    private String address;
    private int contentType;
    private int likeCount;
    private double rating;
    private double latitude;
    private double longitude;
    @JsonProperty("isLiked")
    private boolean isLiked;
    private List<ReviewResponseDTO> review;
}