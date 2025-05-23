package com.ssafy.stella_trip.dao.plan;

import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
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
            @Param("minDuration") int minDuration,
            @Param("maxDuration") int maxDuration,
            @Param("sort") String sort,
            @Param("currentUserId") int currentUserId
    );
    int countPlansByCondition(
            @Param("search") String search,
            @Param("userName") String userName,
            @Param("minDuration") int minDuration,
            @Param("maxDuration") int maxDuration
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

    List<RouteDTO> getRoutesByPlanId(@Param("planId") int planId);

    RouteDTO getRouteByRouteId(@Param("routeId") int routeId);

    int updateStella(
            @Param("planId") int planId,
            @Param("stella") String stella
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

    int insertPlan(@Param("plan") PlanDTO plan);

    int insertTags(@Param("tags") List<String> tags);

    void updateTagCount();

    int linkTagsToPlan(@Param("planId") int planId, @Param("tags") List<String> tags);

    int deletePlanTagByPlanId(@Param("planId") int planId);

    int insertPlanWriter(@Param("planId") int planId, @Param("userId") int userId);

    int deletePlanWriter(@Param("planId") int planId, @Param("userId") int userId);

    int getPlanWritersCount(@Param("planId") int planId);

    boolean checkPlanWriter(@Param("planId") int planId, @Param("userId") int userId);

    int updateBasicPlanInfo(
            @Param("planId") int planId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("isPublic") boolean isPublic
    );
  
    int updateRouteInfo(
            @Param("routeId") int routeId,
            @Param("visitTime") LocalTime visitTime,
            @Param("memo") String memo
    );
  
    int countUserPlansByUserId(int userId);
  
    int countLikedPlansByUserId(int userId);
  
    List<PlanDTO> getUserPlansByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
  
    List<PlanDTO> getLikedPlansByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);

    List<TagDTO> getTagsOrderedByCount(int size);
}
