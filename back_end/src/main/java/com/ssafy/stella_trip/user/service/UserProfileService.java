package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.dao.user.UserProfileDAO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileDAO userProfileDAO;

    /**
     *
     * @param userId 조회할 사용자 id
     * @return 사용자 프로필 정보
     */
    public UserProfileResponseDTO getUserProfile(int userId) {
        return userProfileDAO.getUserProfileByUserId(userId);
    }

    /**
     *
     * @return 자신의 프로필 정보
     */
    public MyProfileResponseDTO getMyProfile() {
        int userId = 0; //자신의 userId 획득
        return userProfileDAO.getMyProfileByUserId(userId);
    }
}
