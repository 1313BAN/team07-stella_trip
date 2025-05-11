package com.ssafy.stella_trip.plan.controller;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.plan.dto.request.PlanScheduleRequestDTO;
import com.ssafy.stella_trip.plan.dto.response.LockSuccessResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.LockStatusResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.PlanResponseDTO;
import com.ssafy.stella_trip.plan.service.PlanService;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/plans")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "여행 계획 검색",
            description = "조건: 페이지, 사이즈, 검색어, 작성자 이름, 여행 기간, 정렬 방식(최신순/인기순)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회 완료"),
    })
    public CommonResponse<PageDTO<PlanResponseDTO>> getPlan(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", defaultValue = "")  String search,
            @RequestParam(value = "username", defaultValue = "") String userName,
            @RequestParam(value = "duration", defaultValue = "0") int duration,
            @RequestParam(value = "sort", defaultValue = "recent") String sort,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.searchPlanByCondition(
                page,
                size,
                search,
                userName,
                duration,
                sort,
                user
        ), HttpStatus.OK);
    }

    @GetMapping("/{planId}")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "여행 계획 상세 조회",
            description = "여행 계획 ID로 여행 계획 상세 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
    })
    public CommonResponse<PlanResponseDTO> getPlanDetail(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.getPlanDetail(planId, user), HttpStatus.OK);
    }

    @PostMapping("/{planId}/like")
    @Operation(
            summary = "좋아요 등록",
            description = "여행 계획 ID로 여행 계획 좋아요 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 등록 완료"),
            @ApiResponse(responseCode = "400", description = "PLAN-002: 중복된 좋아요 등록 / 취소 입니다."),
    })
    public CommonResponse<Void> likePlan(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        planService.likePlan(planId, user);
        return new CommonResponse<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{planId}/like")
    @Operation(
            summary = "좋아요 취소",
            description = "여행 계획 ID로 여행 계획 좋아요 취소"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 취소 완료"),
            @ApiResponse(responseCode = "400", description = "PLAN-002: 중복된 좋아요 등록 / 취소 입니다."),
    })
    public CommonResponse<Void> unlikePlan(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        planService.unlikePlan(planId, user);
        return new CommonResponse<>(null, HttpStatus.OK);
    }

    @GetMapping("/{planId}/lock-check")
    @Operation(
            summary = "여행 계획 Lock 체크",
            description = "여행 계획 ID로 여행 계획 Lock 체크"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 체크 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-004: 해당 계획은 누군가 수정중입니다."),
    })
    public CommonResponse<LockStatusResponseDTO> checkPlanLock(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.checkLock(planId, user), HttpStatus.OK);
    }

    @GetMapping("/{planId}/lock")
    @Operation(
            summary = "여행 계획 Lock",
            description = "여행 계획 ID로 여행 계획 Lock"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 체크 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
    })
    public CommonResponse<LockSuccessResponseDTO> lockPlan(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.lockPlan(planId, user), HttpStatus.OK);
    }

    @GetMapping("/{planId}/unlock")
    @Operation(
            summary = "여행 계획 Unlock",
            description = "여행 계획 ID로 여행 계획 Unlock"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 체크 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
    })
    public CommonResponse<LockSuccessResponseDTO> unlockPlan(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.releaseLock(planId, user), HttpStatus.OK);
    }

    @PutMapping("/{planId}/schedule")
    @Operation(
            summary = "여행 계획 일정 수정",
            description = "여행 계획 ID로 여행 계획 일정 수정, 수정시에 일정 범위 밖의 모든 route는 삭제됨"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
    })
    public CommonResponse<PlanResponseDTO> updatePlanSchedule(
            @PathVariable(value = "planId") int planId,
            @RequestBody PlanScheduleRequestDTO scheduleRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.updatePlanSchedule(planId, scheduleRequestDTO, user), HttpStatus.OK);
    }
}
