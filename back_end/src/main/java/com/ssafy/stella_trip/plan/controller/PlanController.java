package com.ssafy.stella_trip.plan.controller;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.plan.dto.request.*;
import com.ssafy.stella_trip.plan.dto.response.*;
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

import java.util.List;

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
            @RequestParam(value = "search", defaultValue = "") String search,
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

    @PostMapping("/{planId}/lock")
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

    @PostMapping("/{planId}/unlock")
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

    @PostMapping("/{planId}/attraction")
    @Operation(
            summary = "여행 계획 관광지 추가",
            description = "여행 계획 ID로 여행 계획 관광지 추가"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 추가 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "PLAN-005: 잘못된 여행 날짜입니다."),
    })
    public CommonResponse<PlanResponseDTO> addAttraction(
            @PathVariable(value = "planId") int planId,
            @RequestBody RouteInsertRequestDTO routeInsertRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.addAttraction(planId, routeInsertRequestDTO, user), HttpStatus.OK);
    }

    @PatchMapping("/{planId}/attraction")
    @Operation(
            summary = "여행 계획 순서 변경 (수정, 삭제)",
            description = "여행 계획 ID로 여행 계획 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "PLAN-005: 잘못된 여행 날짜입니다."),
    })
    public CommonResponse<PlanResponseDTO> updatePlanRoutes(
            @PathVariable(value = "planId") int planId,
            @RequestBody RoutesUpdateRequestDTO routesUpdateRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.updatePlanRoutes(planId, routesUpdateRequestDTO, user), HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            summary = "여행 계획 추가",
            description = "여행 계획 추가"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 추가 완료"),
    })
    public CommonResponse<PlanResponseDTO> addPlan(
            @RequestBody PlanRequestDTO planRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.addPlan(planRequestDTO, user), HttpStatus.CREATED);
    }

    @PostMapping("/{planId}/leave")
    @Operation(
            summary = "여행 계획 나가기",
            description = "여행 계획 ID로 여행 계획 나가기. 작성자가 없는 plan은 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 나가기 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
    })
    public CommonResponse<Boolean> leavePlan(
            @PathVariable(value = "planId") int planId,
            @AuthenticationPrincipal JwtUserInfo user
    ) {

        return new CommonResponse<>(planService.leavePlan(planId, user), HttpStatus.OK);
    }

    @PostMapping("/{planId}/invite")
    @Operation(
            summary = "여행 계획 초대",
            description = "사용자 이메일로 여행 계획 초대"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 초대 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "PLAN-007: 해당 이메일의 사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "PLAN-008: 해당 사용자는 이미 작성자입니다."),
    })
    public CommonResponse<Boolean> invitePlan(
            @PathVariable(value = "planId") int planId,
            @RequestBody UserInvitationRequestDTO userInvitationRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.invitePlan(planId, userInvitationRequestDTO.getEmail(), user), HttpStatus.OK);
    }

    @PutMapping("/{planId}")
    @Operation(
            summary = "여행 기본정보 수정",
            description = "여행 계획 ID로 여행 기본정보 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
    })
    public CommonResponse<PlanResponseDTO> updatePlan(
            @PathVariable(value = "planId") int planId,
            @RequestBody BasicPlanRequestDTO basicPlanRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.updatePlan(planId, basicPlanRequestDTO, user), HttpStatus.OK);
    }

    @PutMapping("/{planId}/routes/{routeId}")
    @Operation(
            summary = "여행 경유지 상세정보 수정",
            description = "routeId ID로 여행 경유지 상세정보 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정 완료"),
            @ApiResponse(responseCode = "404", description = "PLAN-001: 해당 ID의 계획을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "403", description = "PLAN-003: 해당 계획에 대한 접근 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "PLAN-009: 해당 경로는 계획에 포함되어 있지 않습니다."),
            @ApiResponse(responseCode = "404", description = "PLAN-010: 해당 경로는 존재하지 않습니다."),
    })
    public CommonResponse<RouteResponseDTO> updateRoute(
            @PathVariable(value = "planId") int planId,
            @PathVariable(value = "routeId") int routeId,
            @RequestBody RouteUpdateRequestDTO routeUpdateRequestDTO,
            @AuthenticationPrincipal JwtUserInfo user
    ) {
        return new CommonResponse<>(planService.updateRoute(planId, routeId, routeUpdateRequestDTO, user), HttpStatus.OK);
    }

    @GetMapping("/tags")
    @Operation(
            summary = "여행 계획 태그 목록 조회",
            description = "조건: 페이지, 사이즈, 검색어, 정렬 방식(id순/인기순)"
    )
    @PreAuthorize("permitAll()")
    public CommonResponse<List<TagResponseDTO>> getTags(
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return new CommonResponse<>(planService.getPopularTags(size), HttpStatus.OK);
    }
}
