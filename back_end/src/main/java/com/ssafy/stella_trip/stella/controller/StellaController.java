package com.ssafy.stella_trip.stella.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.stella.dto.request.StellaRequestDTO;
import com.ssafy.stella_trip.stella.dto.response.StellaResponseDTO;
import com.ssafy.stella_trip.stella.service.StellaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("v1/stella")
public class StellaController {

    public final StellaService stellaService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<StellaResponseDTO> createStellaLink(@RequestBody StellaRequestDTO stella, @AuthenticationPrincipal JwtUserInfo user) {
        return new CommonResponse<>(stellaService.createStellaLink(stella, user), HttpStatus.OK);
    }

    @GetMapping("/{link}")
    @PreAuthorize("permitAll()")
    public CommonResponse<StellaResponseDTO> getStella(@PathVariable String link) {
        return new CommonResponse<>(stellaService.getStella(link), HttpStatus.OK);
    }
}
