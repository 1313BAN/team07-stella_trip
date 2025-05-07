package com.ssafy.stella_trip.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyProfileResponseDTO {
    private String name;
    private String email;
    private String description;
    private String image;
    private int follower_count;
    private int following_count;
}
