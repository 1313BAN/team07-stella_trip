package com.ssafy.stella_trip.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRefreshRequestDTO {
    private String refreshToken;
}
