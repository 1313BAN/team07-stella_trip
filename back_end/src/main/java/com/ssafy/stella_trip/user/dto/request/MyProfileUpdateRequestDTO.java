package com.ssafy.stella_trip.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileUpdateRequestDTO {
    private String name;
    private String description;
    private String image;
}
