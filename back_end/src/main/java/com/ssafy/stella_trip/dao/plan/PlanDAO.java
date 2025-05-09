package com.ssafy.stella_trip.dao.plan;

import com.ssafy.stella_trip.plan.dto.PlanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface PlanDAO {
    PlanDTO getPlanById(@Param("planId") int planId, @Param("currentUserId") int currentUserId);
    List<PlanDTO> getPlansByCondition(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("search") String search,
            @Param("userName") String userName,
            @Param("duration") int duration,
            @Param("sort") String sort,
            @Param("currentUserId") int currentUserId
    );
    int countPlansByCondition(
            @Param("search") String search,
            @Param("userName") String userName,
            @Param("duration") int duration

    );

    int increaseLikeCount(@Param("planId") int planId);

    int decreaseLikeCount(@Param("planId") int planId);

    void likePlan(@Param("planId") int planId, @Param("userId") int userId);

    void unlikePlan(@Param("planId") int planId, @Param("userId") int userId);

    boolean isPlanLikedByUser(@Param("planId") int planId, @Param("userId") int userId);
}
