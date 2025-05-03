package com.ssafy.stella_trip.user.dto.request;


import com.ssafy.stella_trip.user.dto.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    String name;
    String email;
    String password;
}
