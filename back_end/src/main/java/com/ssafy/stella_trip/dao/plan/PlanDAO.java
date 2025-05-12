package com.ssafy.stella_trip.dao.plan;

import com.ssafy.stella_trip.plan.dto.PlanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanDAO {
    PlanDTO getPlanById(int planId);
    List<PlanDTO> getPlansByCondition(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("search") String search,
            @Param("userName") String userName,
            @Param("duration") int duration,
            @Param("sort") String sort
    );
    int countPlansByCondition(
            @Param("search") String search,
            @Param("userName") String userName,
            @Param("duration") int duration
    );
    int countUserPlansByUserId(int userId);
    int countLikedPlansByUserId(int userId);
    List<PlanDTO> getUserPlansByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
    List<PlanDTO> getLikedPlansByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
}
