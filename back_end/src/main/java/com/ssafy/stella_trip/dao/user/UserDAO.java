package com.ssafy.stella_trip.dao.user;

import com.ssafy.stella_trip.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * User 테이블 DAO
 */
@Mapper
public interface UserDAO {
    UserDTO getUserByEmail(String email);
    int insertUser(UserDTO user);
}
