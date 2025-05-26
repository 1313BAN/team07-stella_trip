package com.ssafy.stella_trip.stella.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.stella.dto.request.StellaRequestDTO;
import com.ssafy.stella_trip.stella.dto.response.StellaResponseDTO;
import com.ssafy.stella_trip.stella.service.StellaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/stella")
public class StellaController {

    public final StellaService stellaService;

    @PostMapping("/create")
    @Operation(
            summary = "별자리 링크 생성",
            description = "사용자가 별자리 링크를 생성합니다. " +
                    "사용자는 인증된 사용자여야 하며, 요청 본문에 별자리 데이터와 계획 ID를 포함해야 합니다."
    )
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<StellaResponseDTO> createStellaLink(@RequestBody StellaRequestDTO stella, @AuthenticationPrincipal JwtUserInfo user) throws JsonProcessingException {
        return new CommonResponse<>(stellaService.createStellaLink(stella, user), HttpStatus.OK);
    }

    @GetMapping("/{link}")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "별자리 링크 조회",
            description = "사용자가 별자리 링크를 조회합니다. " +
                    "링크는 URL 경로 변수로 전달되며, 인증은 필요하지 않습니다."
    )
    public CommonResponse<StellaResponseDTO> getStella(@PathVariable String link) {
        return new CommonResponse<>(stellaService.getStella(link), HttpStatus.OK);
    }
}
