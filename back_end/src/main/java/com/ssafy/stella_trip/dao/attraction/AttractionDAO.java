package com.ssafy.stella_trip.dao.attraction;

import com.ssafy.stella_trip.attraction.dto.*;
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

    ReviewDTO getReviewByReviewId(int reviewId);
    int countReviewsByAttractionId(int attractionId);
    List<ReviewWithUserNameDTO> getReviewsByAttractionId(@Param("attractionId") int attractionId, @Param("offset") int offset, @Param("size") int size, @Param("userId") int userId);
    int insertReview(ReviewDTO reviewDTO);
    int updateReview(ReviewDTO reviewDTO);
    int deleteReviewByReviewId(int reviewId);
    int increaseReviewLikeCount(int reviewId);
    int decreaseReviewLikeCount(int reviewId);
    int insertLikedReview(@Param("userId") int userId, @Param("attractionId") int attractionId ,@Param("reviewId") int reviewId);
    int deleteLikedReview(@Param("userId") int userId, @Param("attractionId") int attractionId ,@Param("reviewId") int reviewId);
    LikedReviewDTO findLikedReview(@Param("userId") int userId, @Param("reviewId") int reviewId);
}
