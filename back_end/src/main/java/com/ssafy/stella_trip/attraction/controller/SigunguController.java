package com.ssafy.stella_trip.attraction.controller;


import com.ssafy.stella_trip.attraction.dto.response.SigunguResponseDTO;
import com.ssafy.stella_trip.attraction.service.SigunguService;
import com.ssafy.stella_trip.common.response.CommonResponse;
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
    public CommonResponse<SigunguResponseDTO> getSigungu() {
        return new CommonResponse<>(sigunguService.getSigungu(), HttpStatus.OK);
    }
}
