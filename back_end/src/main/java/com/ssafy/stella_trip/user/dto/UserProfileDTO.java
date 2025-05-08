package com.ssafy.stella_trip.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserProfileDTO extends UserDTO {
    private int followerCount;
    private int followingCount;
}
