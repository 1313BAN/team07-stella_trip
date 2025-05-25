package com.ssafy.stella_trip.attraction.service;


import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import com.ssafy.stella_trip.attraction.dto.response.AttractionResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.FeaturedAttractionResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.FeaturedTagResponseDTO;
import com.ssafy.stella_trip.dao.featured.FeaturedDAO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.response.TagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FeaturedService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final FeaturedDAO featuredDAO;

    public FeaturedAttractionResponseDTO getFeaturedAttractions(Integer contentTypeId) {

        String cacheKey = getFeaturedCacheKey("attractions:" + contentTypeId);
        Object value = redisTemplate.opsForValue().get(cacheKey);
        if (value instanceof FeaturedAttractionResponseDTO) {
            // 캐시에서 값을 가져온 경우
            return (FeaturedAttractionResponseDTO) value;
        }

        // 캐시에서 값을 가져오지 못한 경우, DB에서 조회
        List<AttractionDTO> attractions = featuredDAO.getFeatruedAttractionsByContentType(0, contentTypeId);
        // 조회된 데이터를 FeaturedAttractionResponseDTO로 변환
        FeaturedAttractionResponseDTO response = new FeaturedAttractionResponseDTO();
        List<AttractionResponseDTO> responseList = attractions.stream().
                map(this::convertToAttractionResponse)
                .toList();
        response.setFeaturedAttractions(responseList);
        redisTemplate.opsForValue().set(cacheKey, response, 30 , TimeUnit.MINUTES);
        return response;
    }

    public FeaturedTagResponseDTO getFeaturedTags() {
        String cacheKey = getFeaturedCacheKey("tags");
        Object value = redisTemplate.opsForValue().get(cacheKey);
        if (value instanceof FeaturedTagResponseDTO) {
            // 캐시에서 값을 가져온 경우
            return (FeaturedTagResponseDTO) value;
        }

        // 캐시에서 값을 가져오지 못한 경우, DB에서 조회
        List<TagDTO> tagsDTO = featuredDAO.getFeaturedTags();

        List<TagResponseDTO> tags = tagsDTO.stream()
                .map(this::convertToTagResponse)
                .toList();
        FeaturedTagResponseDTO response = new FeaturedTagResponseDTO();
        response.setFeaturedTags(tags);
        redisTemplate.opsForValue().set(cacheKey, response, 30, TimeUnit.MINUTES);
        return response;
    }

    private AttractionResponseDTO convertToAttractionResponse(AttractionDTO attractionDTO) {
        return AttractionResponseDTO.builder()
                .attractionId(attractionDTO.getAttractionId())
                .name(attractionDTO.getTitle())
                .image(attractionDTO.getFirstImage2())
                .address(attractionDTO.getAddr1())
                .contentType(attractionDTO.getContentTypeId())
                .likeCount(attractionDTO.getLikeCount())
                .rating(attractionDTO.getRating())
                .latitude(attractionDTO.getLatitude())
                .longitude(attractionDTO.getLongitude())
                .isLiked(attractionDTO.isLiked())
                .build();
    }

    private TagResponseDTO convertToTagResponse(TagDTO tagDTO) {
        return new TagResponseDTO(tagDTO.getTagId(), tagDTO.getName(), tagDTO.getCount());
    }

    private String getFeaturedCacheKey(String suffix) {
        return "featured:" + suffix ;
    }
}
