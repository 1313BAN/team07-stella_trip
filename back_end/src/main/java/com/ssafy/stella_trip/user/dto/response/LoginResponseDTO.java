package com.ssafy.stella_trip.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private int id;
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;
}
