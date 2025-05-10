package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.UserDTO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

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
}
