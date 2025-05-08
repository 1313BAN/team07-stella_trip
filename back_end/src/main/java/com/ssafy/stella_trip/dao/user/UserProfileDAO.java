package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.UserProfileDTO;
import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileDAO {
    UserProfileDTO getUserProfile(int userId);
    int updateMyProfileByUserId(UserDTO userDTO);
    int updatePasswordByUserId(UserDTO userDTO);
}
