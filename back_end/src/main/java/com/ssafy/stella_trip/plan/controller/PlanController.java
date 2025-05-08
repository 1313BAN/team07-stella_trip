package com.ssafy.stella_trip.plan.controller;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.plan.dto.response.PlanResponseDTO;
import com.ssafy.stella_trip.plan.service.PlanService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/plans")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

//    @GetMapping("/")
//    public CommonResponse<PageDTO<PlanResponseDTO>> getPlan(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "20") int size,
//            @RequestParam(value = "search", defaultValue = "")  String search,
//            @RequestParam(value = "username", defaultValue = "") String userName,
//            @RequestParam(value = "duration", defaultValue = "0") int duration,
//            @RequestParam(value = "sort", defaultValue = "recent") String sort
//    ) {
//        return new CommonResponse<>(planService.searchPlanByCondition(
//                page,
//                size,
//                search,
//                userName,
//                duration,
//                sort
//        ), HttpStatus.OK);
//    }

}
