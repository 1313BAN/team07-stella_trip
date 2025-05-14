package com.ssafy.stella_trip.attraction.service;

import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import com.ssafy.stella_trip.attraction.dto.AttractionWithReviewsDTO;
import com.ssafy.stella_trip.attraction.dto.ReviewDTO;
import com.ssafy.stella_trip.attraction.dto.ReviewWithUserNameDTO;
import com.ssafy.stella_trip.attraction.dto.request.ReviewRequestDTO;
import com.ssafy.stella_trip.attraction.dto.response.AttractionResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.ReviewResponseDTO;
import com.ssafy.stella_trip.attraction.exception.*;
import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.util.PaginationUtils;
import com.ssafy.stella_trip.dao.attraction.AttractionDAO;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.user.dto.response.ActionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttractionService {

    private final AttractionDAO attractionDAO;

    public PageDTO<AttractionResponseDTO> getAttractionsByCondition(
            Integer sidoCode,
            Integer gugunCode,
            Integer contentTypeId,
            String keyword,
            int page,
            int size,
            JwtUserInfo user
    ) {
        int userId = user == null ? -1: user.getUserId();
        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> attractionDAO.getAttractionCountByConditions(sidoCode, gugunCode, contentTypeId, keyword),
                (offset, pageSize) -> attractionDAO.getAttractionByConditions(
                        userId,
                        sidoCode,
                        gugunCode,
                        contentTypeId,
                        keyword,
                        offset,
                        pageSize
                ),
                this::convertAttractionToResponseDTO
        );
    }

    public AttractionResponseDTO getAttractionById(Integer attractionId, JwtUserInfo user) {
        int userId = user == null ? -1 : user.getUserId();
        AttractionWithReviewsDTO attraction = attractionDAO.getAttractionByContentId(userId, attractionId);
        if (attraction == null) {
            throw new AttractionNotFoundException("해당 id의 여행지를 찾을 수 없습니다.");
        }
        return convertAttractionToResponseDTO(attraction);
    }

    /**
     * 여행지의 리뷰 목록 페이징 조회
     * @param attractionId attractionId
     * @param page 페이지
     * @param size 한 페이지 크기
     * @return PageDTO
     */
    public PageDTO<ReviewResponseDTO> getAttractionReviews(int attractionId, int page, int size, int userId){
        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> attractionDAO.countReviewsByAttractionId(attractionId),
                (offset, pageSize) -> attractionDAO.getReviewsByAttractionId(attractionId, offset, pageSize, userId),
                this::convertReviewWithUserNameToResponseDTO
        );
    }

    /**
     * 여행지에 리뷰 추가
     * @param attractionId attractionId
     * @param reviewRequestDTO createReviewRequestDTO
     * @param userId userId
     * @return 생성 성공 여부
     */
    public ActionResponseDTO addAttractionReview(int attractionId, ReviewRequestDTO reviewRequestDTO, int userId) {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .attractionId(attractionId)
                .userId(userId)
                .title(reviewRequestDTO.getTitle())
                .content(reviewRequestDTO.getContent())
                .visitDate(reviewRequestDTO.getVisitDate())
                .rating(reviewRequestDTO.getRating())
                .build();

        return new ActionResponseDTO(attractionDAO.insertReview(reviewDTO) > 0);
    }

    /**
     * 자신이 작성한 여행지의 리뷰 수정
     * @param attractionId attractionId
     * @param reviewId reviewId
     * @param reviewRequestDTO reviewRequestDTO
     * @param userId userId
     * @return 수정 성공 여부
     */
    @Transactional
    public ActionResponseDTO modifyAttractionReview(int attractionId, int reviewId, ReviewRequestDTO reviewRequestDTO, int userId) {
        validateReview(attractionId, reviewId, userId);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .attractionId(attractionId)
                .userId(userId)
                .reviewId(reviewId)
                .title(reviewRequestDTO.getTitle())
                .content(reviewRequestDTO.getContent())
                .visitDate(reviewRequestDTO.getVisitDate())
                .rating(reviewRequestDTO.getRating())
                .build();

        return new ActionResponseDTO(attractionDAO.updateReview(reviewDTO) > 0);
    }

    /**
     * 자신이 작성한 여행지의 리뷰 삭제
     * @param attractionId attractionId
     * @param reviewId reviewId
     * @param userId userId
     * @return 삭제 성공 여부
     */
    @Transactional
    public ActionResponseDTO deleteAttractionReview(int attractionId, int reviewId, int userId) {
        validateReview(attractionId, reviewId, userId);

        return new ActionResponseDTO(attractionDAO.deleteReviewByReviewId(reviewId) > 0);
    }

    /**
     * 리뷰에 좋아요 추가하기
     * @param attractionId attractionId
     * @param reviewId reviewId
     * @param userId userId
     * @return 좋아요 추가 성공 여부
     */
    @Transactional
    public ActionResponseDTO addLikeToAttractionReview(int attractionId, int reviewId, int userId) {
        ReviewDTO existingReview = attractionDAO.getReviewByReviewId(reviewId);

        if (existingReview == null) {
            throw new ReviewNotFoundException("해당 id의 리뷰를 찾을 수 없습니다.");
        }

        if (existingReview.getAttractionId() != attractionId) {
            throw new ReviewNotMatchToAttractionException("해당 리뷰의 id와 여행지 id가 일치하지 않습니다.");
        }

        if (attractionDAO.findLikedReview(userId, reviewId) != null) {
            throw new ReviewAlreadyLikedException("이미 좋아요를 누른 리뷰입니다.");
        }

        int insertResult = attractionDAO.insertLikedReview(userId, attractionId, reviewId);

        if (insertResult > 0) {
            attractionDAO.increaseReviewLikeCount(reviewId);
        }

        return new ActionResponseDTO(insertResult > 0);
    }

    /**
     * 리뷰에 좋아요 제거하기
     * @param attractionId attractionId
     * @param reviewId reviewId
     * @param userId userId
     * @return 좋아요 제거 성공 여부
     */
    @Transactional
    public ActionResponseDTO removeLikeFromAttractionReview(int attractionId, int reviewId, int userId) {
        ReviewDTO existingReview = attractionDAO.getReviewByReviewId(reviewId);

        if (existingReview == null) {
            throw new ReviewNotFoundException("해당 id의 리뷰를 찾을 수 없습니다.");
        }

        if (existingReview.getAttractionId() != attractionId) {
            throw new ReviewNotMatchToAttractionException("해당 리뷰의 id와 여행지 id가 일치하지 않습니다.");
        }

        if (attractionDAO.findLikedReview(userId, reviewId) == null) {
            throw new ReviewNotLikedException("좋아요를 누르지 않은 리뷰입니다.");
        }

        int deleteResult = attractionDAO.deleteLikedReview(userId, attractionId, reviewId);

        if (deleteResult > 0) {
            attractionDAO.decreaseReviewLikeCount(reviewId);
        }

        return new ActionResponseDTO(deleteResult > 0);
    }

    /**
     * 여행지에 좋아요 추가하기
     * @param attractionId attractionId
     * @param userId userId
     * @return 좋아요 추가 성공 여부
     */
    @Transactional
    public ActionResponseDTO addLikeToAttraction(int attractionId, int userId) {
        AttractionDTO existingAttraction = attractionDAO.getAttractionById(attractionId);

        if(existingAttraction == null) {
            throw new AttractionNotFoundException("해당 id의 여행지를 찾을 수 없습니다.");
        }

        if (attractionDAO.findLikedAttraction(attractionId, userId) != null) {
            throw new AttractionAlreadyLikedException("이미 좋아요를 누른 여행지입니다.");
        }

        int insertResult = attractionDAO.insertLikedAttraction(attractionId, userId);

        if(insertResult > 0) {
            attractionDAO.increaseAttractionLikeCount(attractionId);
        }

        return new ActionResponseDTO(insertResult > 0);
    }

    /**
     * 여행지에 좋아요 제거하기
     * @param attractionId attractionId
     * @param userId userId
     * @return 좋아요 제거 성공 여부
     */
    @Transactional
    public ActionResponseDTO removeLikeFromAttraction(int attractionId, int userId) {
        AttractionDTO existingAttraction = attractionDAO.getAttractionById(attractionId);

        if(existingAttraction == null) {
            throw new AttractionNotFoundException("해당 id의 여행지를 찾을 수 없습니다.");
        }

        if (attractionDAO.findLikedAttraction(attractionId, userId) == null) {
            throw new AttractionNotLikedException("좋아요를 누르지 않은 여행지입니다.");
        }

        int deleteResult = attractionDAO.deleteLikedAttraction(attractionId, userId);

        if(deleteResult > 0) {
            attractionDAO.decreaseAttractionLikeCount(attractionId);
        }

        return new ActionResponseDTO(deleteResult > 0);
    }


    /**
     * ReviewWithUserNameDTO 를 ReviewResponseDTO 로 변환
     * @param review ReviewWithUserNameDTO
     * @return ReviewResponseDTO
     */
    private ReviewResponseDTO convertReviewWithUserNameToResponseDTO(ReviewWithUserNameDTO review) {
        return ReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUserId())
                .userName(review.getUserName())
                .title(review.getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .visitDate(review.getVisitDate())
                .createdAt(review.getCreatedAt())
                .isLiked(review.isLiked())
                .build();
    }

    /**
     * 접근하려는 리뷰에 대한 일관성, 권한 확인
     * @param attractionId attractionId
     * @param reviewId reviewId
     * @param userId userId
     */
    private void validateReview(int attractionId, int reviewId, int userId) {
        ReviewDTO existingReview = attractionDAO.getReviewByReviewId(reviewId);
        if (existingReview == null) {
            throw new ReviewNotFoundException("해당 id의 리뷰를 찾을 수 없습니다.");
        }

        if(existingReview.getAttractionId() != attractionId){
            throw new ReviewNotMatchToAttractionException("해당 리뷰의 id와 여행지 id가 일치하지 않습니다.");
        }

        if(existingReview.getUserId() != userId){
            throw new ReviewWriterNotMatchToUser("해당 리뷰에 대한 권한이 없습니다.");
        }
    }

    /**
     * AttractionDTO를 AttractionResponseDTO로 변환
     * @param attraction attractionDTO
     * @return AttractionResponseDTO
     */
    private AttractionResponseDTO convertAttractionToResponseDTO(AttractionWithReviewsDTO attraction) {
        return AttractionResponseDTO.builder()
                .attractionId(attraction.getAttractionId())
                .name(attraction.getTitle())
                .image(attraction.getFirstImage1())
                .address(attraction.getAddr1())
                .contentType(attraction.getContentTypeId())
                .like(attraction.getLikeCount())
                .rating(attraction.getRating())
                .latitude(attraction.getLatitude())
                .longitude(attraction.getLongitude())
                .review(attraction.getReviews())
                .isLiked(attraction.isLiked())
                .build();
    }
}
