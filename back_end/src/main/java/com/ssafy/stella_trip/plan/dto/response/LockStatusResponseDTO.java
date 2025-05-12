package com.ssafy.stella_trip.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockStatusResponseDTO {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("plan_id")
    private int planId;
    @JsonProperty("lock_status")
    private boolean lockStatus;
}
