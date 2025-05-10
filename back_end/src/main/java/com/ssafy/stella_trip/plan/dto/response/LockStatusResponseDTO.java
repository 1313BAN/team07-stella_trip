package com.ssafy.stella_trip.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockStatusResponseDTO {
    private int userId;
    private int planId;
    private boolean lockStatus;
}
