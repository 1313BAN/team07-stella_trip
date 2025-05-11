package com.ssafy.stella_trip.dao.plan;

import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
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

    int updatePlanSchedule(
            @Param("planId") int planId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    int deleteRoutesExceedingDayIndex(
            @Param("planId") int planId
    );

    int deletePlan(
            @Param("planId") int planId
    );

    int insertRoute(
            @Param("planId") int planId,
            @Param("attractionId") int attractionId,
            @Param("dayIndex") int dayIndex,
            @Param("order") int order,
            @Param("visitTime") LocalTime visitTime,
            @Param("memo") String memo
    );

    int updateRoutes(@Param("routes") List<RouteDTO> routes);

    int deleteRoutes(@Param("routes") List<RouteDTO> routes);
}
