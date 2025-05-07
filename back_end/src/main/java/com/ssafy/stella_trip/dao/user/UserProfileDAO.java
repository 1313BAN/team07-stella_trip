package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.UserProfileDTO;
import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileDAO {
    UserProfileDTO getUserProfile(int userId);
    int updateMyProfileByUserId(int userId, MyProfileUpdateRequestDTO myProfileUpdateRequestDTO);
    int updatePasswordByUserId(int userId, String password);
}
