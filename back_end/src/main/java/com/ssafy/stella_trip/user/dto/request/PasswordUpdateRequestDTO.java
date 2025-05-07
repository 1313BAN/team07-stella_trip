package com.ssafy.stella_trip.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDTO {
    @NotBlank
    private String password;
}
