package com.ssafy.stella_trip.attraction.dto.response;

import com.ssafy.stella_trip.plan.dto.response.TagResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedTagResponseDTO {
    List<TagResponseDTO> featuredTags;
}
