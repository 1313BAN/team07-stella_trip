package com.ssafy.stella_trip.dao;

import com.ssafy.stella_trip.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    UserDTO getUserByEmail(String email);
}
