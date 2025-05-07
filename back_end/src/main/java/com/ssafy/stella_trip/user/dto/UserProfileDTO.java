package com.ssafy.stella_trip.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class UserProfileDTO extends UserDTO{
    private int followerCount;
    private int followingCount;

    UserProfileDTO(int userId, String name, String email, UserRole role, String password, String description, String image, LocalDateTime createdAt, LocalDateTime modifiedAt, int followerCount, int followingCount) {
        super(userId, name, email, role, password, description, image, createdAt, modifiedAt);
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}
