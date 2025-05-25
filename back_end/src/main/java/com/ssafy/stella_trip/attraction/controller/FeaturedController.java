package com.ssafy.stella_trip.attraction.controller;


import com.ssafy.stella_trip.attraction.dto.response.FeaturedAttractionResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.FeaturedTagResponseDTO;
import com.ssafy.stella_trip.attraction.service.FeaturedService;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
@RequestMapping("/v1/featured")
public class FeaturedController {

    private final FeaturedService featuredService;

    @GetMapping("/attraction/contentType/{contentTypeId}")
    public CommonResponse<FeaturedAttractionResponseDTO> getFeaturedAttractions(@PathVariable Integer contentTypeId) {
        return new CommonResponse<>(featuredService.getFeaturedAttractions(contentTypeId), HttpStatus.OK);
    }

    @GetMapping("/tags")
    public CommonResponse<FeaturedTagResponseDTO> getFeaturedTags() {
        return new CommonResponse<>(featuredService.getFeaturedTags(), HttpStatus.OK);
    }
}
