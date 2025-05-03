package com.ssafy.stella_trip.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    int userId;
    String name;
    String email;
    UserRole role;
    String password;
    String description;
    String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
