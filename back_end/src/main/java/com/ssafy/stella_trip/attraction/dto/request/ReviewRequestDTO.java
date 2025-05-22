package com.ssafy.stella_trip.attraction.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private String title;
    private String content;
    private double rating;
    private LocalDate visitDate;
}
