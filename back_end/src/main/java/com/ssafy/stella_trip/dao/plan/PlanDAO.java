package com.ssafy.stella_trip.dao.plan;

import com.ssafy.stella_trip.plan.dto.PlanDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanDAO {
    PlanDTO getPlanById(int planId);

}
