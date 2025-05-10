package com.ssafy.stella_trip.user.controller;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.plan.dto.response.PlanResponseDTO;
import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.request.PasswordUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import com.ssafy.stella_trip.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserProfileController {

    private final UserProfileService userProfileService;

    //자신의 프로필 조회
    @GetMapping("/profile")
    public CommonResponse<MyProfileResponseDTO> getMyProfile() {
        return new CommonResponse<>(userProfileService.getMyProfile(), HttpStatus.OK);
    }

    //다른 유저의 프로필 조회
    @GetMapping("/{userId}")
    @PreAuthorize("permitAll()")
    public CommonResponse<UserProfileResponseDTO> getUserProfile(@PathVariable int userId) {
        return new CommonResponse<>(userProfileService.getUserProfile(userId), HttpStatus.OK);
    }

    //자신의 프로필 수정
    @PutMapping("/profile")
    public CommonResponse<MyProfileResponseDTO> updateMyProfile(@RequestBody MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) {
        return new CommonResponse<>(userProfileService.updateMyProfile(myProfileUpdateRequestDTO), HttpStatus.OK);
    }

    //자신의 비밀번호 수정
    @PutMapping("/password")
    public CommonResponse<Void> updatePassword(@RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        userProfileService.updatePassword(passwordUpdateRequestDTO.getPassword());
        return new CommonResponse<>(null, HttpStatus.NO_CONTENT);
    }

    //자신의 여행 계획 목록 조회
    @GetMapping("/plans")
    public CommonResponse<PageDTO<PlanResponseDTO>> getUserPlans(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "size", defaultValue = "20") int size){
        return new CommonResponse<>(userProfileService.getUserPlans(page, size), HttpStatus.OK);
    }

    //자신이 좋아요한 여행 계획 목록 조회
    @GetMapping("/liked/plans")
    public CommonResponse<PageDTO<PlanResponseDTO>> getLikedPlans(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size){
        return new CommonResponse<>(userProfileService.getLikedPlans(page, size), HttpStatus.OK);
    }
}
