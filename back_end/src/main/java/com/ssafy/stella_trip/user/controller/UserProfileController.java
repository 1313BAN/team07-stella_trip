package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.request.PasswordUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import com.ssafy.stella_trip.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    //자신의 프로필 수정
    @PutMapping("/profile")
    CommonResponse<MyProfileResponseDTO> updateMyProfile(@RequestBody MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) {
        return new CommonResponse<>(userProfileService.updateMyProfile(myProfileUpdateRequestDTO), HttpStatus.OK);
    }

    //자신의 비밀번호 수정
    @PutMapping("/password")
    CommonResponse<Void> updatePassword(@RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        userProfileService.updatePassword(passwordUpdateRequestDTO.getPassword());
        return new CommonResponse<>(null, HttpStatus.NO_CONTENT);
    }
}
