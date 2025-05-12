package com.ssafy.stella_trip.user.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FollowUserDTO extends UserDTO {
    private LocalDateTime followsCreatedAt;
}
