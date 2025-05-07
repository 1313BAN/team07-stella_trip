package com.ssafy.stella_trip.security.dto;

import com.ssafy.stella_trip.user.dto.UserRole;
import lombok.Data;

@Data
public class JwtUserInfo {
    private final int userId;
    private final String email;
}
