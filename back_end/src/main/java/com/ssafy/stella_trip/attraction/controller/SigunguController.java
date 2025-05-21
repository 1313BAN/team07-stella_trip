package com.ssafy.stella_trip.attraction.controller;


import com.ssafy.stella_trip.attraction.dto.response.SigunguResponseDTO;
import com.ssafy.stella_trip.attraction.service.SigunguService;
import com.ssafy.stella_trip.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/attractions")
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
public class SigunguController {

    private final SigunguService sigunguService;

    @GetMapping("/sigungu")
    @Operation(
            summary = "시군구 정보 조회",
            description = "시군구 정보 조회 api입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회 완료"),
    })
    public CommonResponse<SigunguResponseDTO> getSigungu() {
        return new CommonResponse<>(sigunguService.getSigungu(), HttpStatus.OK);
    }
}
