package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;

public interface UserProfileDAO {
    UserProfileResponseDTO getUserProfileByUserId(int userId);
    MyProfileResponseDTO getMyProfileByUserId(int userId);
    int updateMyProfileByUserId(int userId, MyProfileUpdateRequestDTO myProfileUpdateRequestDTO);
    int updatePasswordByUserId(int userId, String password);
}
