package com.ssafy.stella_trip.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserProfileDTO extends UserDTO{
    private int followerCount;
    private int followingCount;
}
