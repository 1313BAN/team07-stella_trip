package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.FollowUserDTO;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.UserProfileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User 테이블 DAO
 */
@Mapper
public interface UserDAO {
    List<UserDTO> getWritersByPlanId(int planId);
    UserDTO getUserByEmail(String email);
    int insertUser(UserDTO user);
    int deleteUserByUserId(int userId);
    UserProfileDTO getUserProfile(int userId);
    int updateMyProfileByUserId(UserDTO userDTO);
    int updatePasswordByUserId(UserDTO userDTO);
    int countFollowingsByUserId(@Param("userId") int userId);
    int countFollowersByUserId(@Param("userId") int userId);
    List<FollowUserDTO> getFollowingsByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
    List<FollowUserDTO> getFollowersByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
}
