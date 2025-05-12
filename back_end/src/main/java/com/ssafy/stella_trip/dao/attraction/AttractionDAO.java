package com.ssafy.stella_trip.dao.attraction;

import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import com.ssafy.stella_trip.attraction.dto.AttractionWithReviewsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttractionDAO {
    AttractionDTO getAttractionById(int attractionId);
    List<AttractionDTO> getAttractionsBySidoCode(int sidoCode);
    List<AttractionDTO> getAttractionsBySidoAndGugunCode(@Param("sidoCode") int sidoCode,
                                                         @Param("gugunCode") int gugunCode);
    List<AttractionDTO> getAttractionsByContentTypeId(int contentTypeId);
    int countLikedAttractionsByUserId(int userId);
    List<AttractionWithReviewsDTO> getLikedAttractionsWithReviews(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
}
