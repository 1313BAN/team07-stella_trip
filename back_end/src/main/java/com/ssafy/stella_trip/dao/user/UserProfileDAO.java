package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;

public interface UserProfileDAO {
    UserProfileResponseDTO getUserProfileByUserId(int userId);
    MyProfileResponseDTO getMyProfileByUserId(int userId);
}
