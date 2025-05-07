package com.ssafy.stella_trip.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {
    private String name;
    private String description;
    private String image;
    private int followerCount;
    private int followingCount;
}
