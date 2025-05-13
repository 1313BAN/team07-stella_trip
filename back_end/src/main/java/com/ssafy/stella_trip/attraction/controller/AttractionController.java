package com.ssafy.stella_trip.attraction.controller;

import com.ssafy.stella_trip.attraction.dto.request.ReviewRequestDTO;
import com.ssafy.stella_trip.attraction.dto.response.ReviewResponseDTO;
import com.ssafy.stella_trip.attraction.service.AttractionService;
import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.user.dto.response.ActionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/attractions")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    //리뷰 페이징 조회
    @GetMapping("/{attractionId}/reviews")
    public CommonResponse<PageDTO<ReviewResponseDTO>> getAttractionReviews(
            @PathVariable int attractionId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @AuthenticationPrincipal JwtUserInfo user
            ) {
        return new CommonResponse<>(attractionService.getAttractionReviews(attractionId, page, size, user.getUserId()), HttpStatus.OK);
    }

    //리뷰 생성
    @PostMapping("/{attractionId}/reviews")
    public CommonResponse<ActionResponseDTO> addAttractionReview(
            @PathVariable int attractionId,
            @RequestBody ReviewRequestDTO reviewRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(attractionService.addAttractionReview(attractionId, reviewRequestDTO, user.getUserId()), HttpStatus.CREATED);
    }

    //리뷰 수정
    @PutMapping("/{attractionId}/reviews/{reviewId}")
    public CommonResponse<ActionResponseDTO> modifyAttractionReview(
            @PathVariable int attractionId,
            @PathVariable int reviewId,
            @RequestBody ReviewRequestDTO reviewRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(attractionService.modifyAttractionReview(attractionId, reviewId, reviewRequestDTO, user.getUserId()), HttpStatus.OK);
    }

    //리뷰 삭제
    @DeleteMapping("/{attractionId}/reviews/{reviewId}")
    public CommonResponse<ActionResponseDTO> deleteAttractionReview(
            @PathVariable int attractionId,
            @PathVariable int reviewId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(attractionService.deleteAttractionReview(attractionId, reviewId, user.getUserId()), HttpStatus.OK);
    }

    //리뷰 좋아요 추가
    @PostMapping("/{attractionId}/reviews/{reviewId}/like")
    public CommonResponse<ActionResponseDTO> addLikeToAttractionReview(
            @PathVariable int attractionId,
            @PathVariable int reviewId,
            @AuthenticationPrincipal JwtUserInfo user
    ){
        return new CommonResponse<>(attractionService.addLikeToAttractionReview(attractionId, reviewId, user.getUserId()), HttpStatus.OK);
    }

    //리뷰 좋아요 삭제
    @DeleteMapping("/{attractionId}/reviews/{reviewId}/like")
    public CommonResponse<ActionResponseDTO> removeLikeFromAttractionReview(
            @PathVariable int attractionId,
            @PathVariable int reviewId,
            @AuthenticationPrincipal JwtUserInfo user
    ){
        return new CommonResponse<>(attractionService.removeLikeFromAttractionReview(attractionId, reviewId, user.getUserId()), HttpStatus.OK);
    }

    //여행지 좋아요 추가
    @PostMapping("/{attractionId}/like")
    public CommonResponse<ActionResponseDTO> addLikeToAttraction(
            @PathVariable int attractionId,
            @AuthenticationPrincipal JwtUserInfo user
    ){
        return new CommonResponse<>(attractionService.addLikeToAttraction(attractionId, user.getUserId()), HttpStatus.OK);
    }

    //여행지 좋아요 삭제
    @DeleteMapping("/{attractionId}/like")
    public CommonResponse<ActionResponseDTO> removeLikeFromAttraction(
            @PathVariable int attractionId,
            @AuthenticationPrincipal JwtUserInfo user
    ){
        return new CommonResponse<>(attractionService.removeLikeFromAttraction(attractionId, user.getUserId()), HttpStatus.OK);
    }
}
