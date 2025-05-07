package com.ssafy.stella_trip.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileUpdateRequestDTO {
    @Size(max = 20, message = "이름은 20자를 초과할 수 없습니다")
    private String name;
    @Size(max = 200, message = "설명은 200자를 초과할 수 없습니다")
    private String description;
    private String image;
}
