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
    @JsonProperty("attraction_id")
    private int attractionId;
    private String name;
    private String image;
    private String address;
    @JsonProperty("content_type")
    private String contentType;
    private int like;
    private double rating;
    private double latitude;
    private double longitude;
    private List<ReviewResponseDTO> review;
}