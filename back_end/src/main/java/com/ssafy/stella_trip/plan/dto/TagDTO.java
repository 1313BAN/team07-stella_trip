package com.ssafy.stella_trip.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private int tagId;
    private String name;
    private int count;
}
