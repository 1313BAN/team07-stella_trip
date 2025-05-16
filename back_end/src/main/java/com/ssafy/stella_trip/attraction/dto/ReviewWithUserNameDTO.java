package com.ssafy.stella_trip.attraction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReviewWithUserNameDTO extends ReviewDTO {
    String userName;
}
