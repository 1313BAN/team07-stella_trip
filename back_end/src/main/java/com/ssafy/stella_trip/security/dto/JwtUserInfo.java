package com.ssafy.stella_trip.security.dto;

import com.ssafy.stella_trip.user.dto.UserRole;
import lombok.Data;

import java.security.Principal;

@Data
public class JwtUserInfo implements Principal {
    private final int userId;
    private final String username;
    private final String email;

    @Override
    public String getName() {
        return this.username;
    }
}
