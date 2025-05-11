package com.ssafy.stella_trip.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowResponseDTO {
    private int userId;
    private String name;
    private String image;
    private String description;
}
