package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import com.ssafy.stella_trip.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    //자신의 프로필 조회
    @GetMapping
    CommonResponse<MyProfileResponseDTO> getMyProfile() {
        return new CommonResponse<>(userProfileService.getMyProfile(), HttpStatus.OK);
    }

    //다른 유저의 프로필 조회
    @GetMapping("/{userId}")
    CommonResponse<UserProfileResponseDTO> getUserProfile(@PathVariable int userId) {
        return new CommonResponse<>(userProfileService.getUserProfile(userId), HttpStatus.OK);
    }
}
