package com.ssafy.stella_trip.user.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * UserDAO에서 user 정보를 가져오기 위한 DTO <p/>
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class UserDTO {
    private int userId;
    private String name;
    private String email;
    private UserRole role;
    private String password;
    private String description;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
