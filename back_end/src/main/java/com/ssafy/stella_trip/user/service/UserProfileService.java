package com.ssafy.stella_trip.user.service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.ssafy.stella_trip.dao.user.UserProfileDAO;
import com.ssafy.stella_trip.user.dto.UserProfileDTO;
import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import com.ssafy.stella_trip.user.exception.ProfileNotFoundException;
import com.ssafy.stella_trip.user.exception.ProfileUpdateFailureException;
import com.ssafy.stella_trip.user.exception.PasswordUpdateFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileDAO userProfileDAO;
    private final PasswordEncoder passwordEncoder;

    private int getCurrentAuthenticatedUserId() {
        //TODO: 자신의 userId 획득
        return 1;
    }


    /**
     * 다른 사용자의 프로필 정보 조회
     * @param userId 조회할 사용자 id
     * @return 사용자 프로필 정보
     * @throws ProfileNotFoundException 프로필을 찾을 수 없는 경우
     */
    public UserProfileResponseDTO getUserProfile(int userId) {
        UserProfileDTO profile = userProfileDAO.getUserProfile(userId);

        if (profile == null) {
            throw new ProfileNotFoundException("사용자 ID " + userId + "에 해당하는 프로필을 찾을 수 없습니다.");
        }

        UserProfileResponseDTO userProfileResponseDTO = UserProfileResponseDTO.builder()
                .name(profile.getName())
                .description(profile.getDescription())
                .image(profile.getImage())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .build();

        return userProfileResponseDTO;
    }

    /**
     * 자신의 프로필 정보 조회
     * @return 자신의 프로필 정보
     * @throws ProfileNotFoundException 프로필을 찾을 수 없는 경우
     */
    public MyProfileResponseDTO getMyProfile() {
        int userId = getCurrentAuthenticatedUserId();

        UserProfileDTO profile = userProfileDAO.getUserProfile(userId);

        if (profile == null) {
            throw new ProfileNotFoundException("자신의 프로필을 찾을 수 없습니다.");
        }

        MyProfileResponseDTO myProfileResponseDTO = MyProfileResponseDTO.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .description(profile.getDescription())
                .image(profile.getImage())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .build();

        return myProfileResponseDTO;
    }

    /**
     * 자신의 프로필 정보 업데이트
     * @param myProfileUpdateRequestDTO 업데이트할 프로필 정보
     * @return 업데이트된 자신의 프로필 정보
     * @throws ProfileUpdateFailureException 프로필 업데이트 실패 시
     * @throws ProfileNotFoundException 업데이트 후 프로필 조회 실패 시
     */
    public MyProfileResponseDTO updateMyProfile(MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) {
        int userId = getCurrentAuthenticatedUserId();

        // 업데이트 수행 및 결과 확인
        int affectedRows = userProfileDAO.updateMyProfileByUserId(userId, myProfileUpdateRequestDTO);
        if (affectedRows == 0) {
            throw new ProfileUpdateFailureException("프로필 업데이트에 실패했습니다.");
        }

        // 업데이트된 프로필 조회
        UserProfileDTO profile = userProfileDAO.getUserProfile(userId);
        if (profile == null) {
            throw new ProfileNotFoundException("업데이트된 프로필을 찾을 수 없습니다.");
        }

        MyProfileResponseDTO myProfileResponseDTO = MyProfileResponseDTO.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .description(profile.getDescription())
                .image(profile.getImage())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .build();

        return myProfileResponseDTO;
    }

    /**
     * 자신의 비밀번호 업데이트
     * @param password 새로운 비밀번호
     * @return 비밀번호 변경 성공, 실패 여부
     * @throws PasswordUpdateFailureException 비밀번호 업데이트 실패 시
     */
    public boolean updatePassword(String password){
        int userId = getCurrentAuthenticatedUserId();

        String encodedPassword = passwordEncoder.encode(password);
        int affectedRows = userProfileDAO.updatePasswordByUserId(userId, encodedPassword);
        if (affectedRows == 0) {
            throw new PasswordUpdateFailureException("비밀번호 업데이트에 실패했습니다.");
        }

        return true;
    }
}