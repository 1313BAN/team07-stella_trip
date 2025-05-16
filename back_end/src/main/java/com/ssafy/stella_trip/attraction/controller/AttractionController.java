package com.ssafy.stella_trip.attraction.controller;

import com.ssafy.stella_trip.attraction.dto.request.ReviewRequestDTO;
import com.ssafy.stella_trip.attraction.dto.response.AttractionResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.ReviewResponseDTO;
import com.ssafy.stella_trip.attraction.service.AttractionService;
import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.user.dto.response.ActionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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

    @GetMapping()
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "여행지 필터링 조회",
            description = "여행지 필터링 조회 API입니다. \n" +
                    "여행지의 시도, 구군, 콘텐츠 타입, 키워드로 여행지를 필터링하여 조회합니다. \n" +
                    "페이징 처리가 되어 있습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회 완료"),
    })
    public CommonResponse<PageDTO<AttractionResponseDTO>> getAttractions(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sidoCode", defaultValue = "") Integer sidoCode,
            @RequestParam(value = "gugunCode", defaultValue = "") Integer gugunCode,
            @RequestParam(value = "contentTypeId", defaultValue = "") Integer contentTypeId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(attractionService.getAttractionsByCondition(sidoCode, gugunCode, contentTypeId, keyword, page, size, user), HttpStatus.OK);
    }

    @GetMapping("/{attractionId}")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "여행지 상세 조회",
            description = "여행지 상세 조회 API입니다. \n" +
                    "여행지 ID로 여행지를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회 완료"),
            @ApiResponse(responseCode = "404", description = "ATTRACTION-006: 여행지 ID가 존재하지 않음"),
    })
    public CommonResponse<AttractionResponseDTO> getAttraction(
            @PathVariable int attractionId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(attractionService.getAttractionById(attractionId, user), HttpStatus.OK);
    }

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
