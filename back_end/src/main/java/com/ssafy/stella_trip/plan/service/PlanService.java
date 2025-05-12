package com.ssafy.stella_trip.plan.service;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.request.*;
import com.ssafy.stella_trip.plan.dto.response.*;
import com.ssafy.stella_trip.plan.exception.*;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.util.PlanLockUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanDAO planDAO;
    private final UserDAO userDAO;
    private final PlanLockUtil planLockUtil;
    private final int LOCK_TIMEOUT = 180; // 3분

    public PageDTO<PlanResponseDTO> searchPlanByCondition(
            int page,
            int size,
            String search,
            String userName,
            int duration,
            String sort,
            JwtUserInfo user
    ){
        // 전체 검색수
        int totalCount = planDAO.countPlansByCondition(search, userName, duration);
        // 전체 페이지 수
        int totalPages = (int) Math.ceil((double) totalCount / size);
        // offset 계산
        int offset = (page - 1) * size;
        // 다음 페이지 여부
        boolean hasNext = (page < totalPages);
        // 첫 페이지 여부
        boolean isFirst = (page == 1);
        // 마지막 페이지 여부
        boolean isLast = (page == totalPages);

        // 검색 결과
        List<PlanDTO> planDTOList = planDAO.getPlansByCondition(offset, size, search, userName, duration, sort, user != null ? user.getUserId() : -1);
        List<PlanResponseDTO> planResponseDTOList = new ArrayList<>();
        for (PlanDTO planDTO : planDTOList) {
            // 태그 리스트
            List<TagResponseDTO> tagResponseDTOList = convertTagsToResponse(planDTO.getTags());

            // 작성자 리스트
            List<WriterResponseDTO> writerResponseDTOList = convertWritersToResponse(planDTO.getWriters());

            // PlanResponseDTO 생성
            PlanResponseDTO planResponseDTO = PlanResponseDTO.builder()
                    .planId(planDTO.getPlanId())
                    .title(planDTO.getTitle())
                    .description(planDTO.getDescription())
                    .stella(planDTO.getStella())
                    .startDate(planDTO.getStartDate())
                    .endDate(planDTO.getEndDate())
                    .likeCount(planDTO.getLikeCount())
                    .isPublic(planDTO.isPublic())
                    .planWriters(writerResponseDTOList)
                    .tags(tagResponseDTOList)
                    .liked(planDTO.isLiked())
                    .build();

            planResponseDTOList.add(planResponseDTO);
        }

        return PageDTO.<PlanResponseDTO>builder()
                .content(planResponseDTOList)
                .hasNext(hasNext)
                .totalPages(totalPages)
                .totalElements(totalCount)
                .page(page)
                .size(size)
                .isFirst(isFirst)
                .isLast(isLast)
                .build();
    }

    public PlanDetailResponseDTO getPlanDetail(int planId, JwtUserInfo user) {
        // PlanDTO 가져오기
        PlanDTO planDTO = planDAO.getPlanById(planId, user != null ? user.getUserId() : -1);
        if (planDTO == null) {
            throw new PlanNotFoundException("해당 ID의 계획을 찾을 수 없습니다. planId: " + planId);
        }

        // 권한 체크
        if (!planDTO.isPublic()) {
            if(user == null) {
                throw new UnauthorizedPlanAccessException("해당 계획에 대한 접근 권한이 없습니다. planId: " + planId);
            }else if (planDTO.getWriters().stream().noneMatch(writer -> writer.getUserId() == user.getUserId())) {
                throw new UnauthorizedPlanAccessException("해당 계획에 대한 접근 권한이 없습니다. planId: " + planId);
            }
        }

        // 태그 리스트
        List<TagResponseDTO> tagResponseDTOList = convertTagsToResponse(planDTO.getTags());

        // 작성자 리스트
        List<WriterResponseDTO> writerResponseDTOList = convertWritersToResponse(planDTO.getWriters());

        // 루트 리스트
        Map<LocalDate, List<RouteResponseDTO>> routeResponseDTOMap = convertRoutesToResponse(planDTO.getRoutes(), planDTO.getStartDate());

        // PlanDetailResponseDTO 생성
        PlanDetailResponseDTO planDetailResponseDTO = PlanDetailResponseDTO.builder()
                .planId(planDTO.getPlanId())
                .title(planDTO.getTitle())
                .description(planDTO.getDescription())
                .stella(planDTO.getStella())
                .startDate(planDTO.getStartDate())
                .endDate(planDTO.getEndDate())
                .likeCount(planDTO.getLikeCount())
                .isPublic(planDTO.isPublic())
                .planWriters(writerResponseDTOList)
                .tags(tagResponseDTOList)
                .details(routeResponseDTOMap)
                .liked(planDTO.isLiked())
                .build();

        return planDetailResponseDTO;
    }

    @Transactional
    public void likePlan(int planId, JwtUserInfo user) {
        if(planDAO.isPlanLikedByUser(planId, user.getUserId())) {
            throw new DuplicatedLikeException("이미 좋아요를 누른 계획입니다.");
        }
        planDAO.likePlan(planId, user.getUserId());
        planDAO.increaseLikeCount(planId);
    }

    @Transactional
    public void unlikePlan(int planId, JwtUserInfo user) {
        if(!planDAO.isPlanLikedByUser(planId, user.getUserId())) {
            throw new DuplicatedLikeException("이미 좋아요가 없습니다..");
        }
        planDAO.unlikePlan(planId, user.getUserId());
        planDAO.decreaseLikeCount(planId);
    }

    @Transactional
    public LockStatusResponseDTO checkLock(int planId, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // redis에서 lock 상태 확인
        Integer val = planLockUtil.checkPlanLock(planId);

        // 락이 없으면 false, 있으면 true 반환
        boolean lockStatus = val != null;
        int lockedUserId = lockStatus ? val : -1; // 락이 걸려있다면 userId를 파싱

        return new LockStatusResponseDTO(lockedUserId, planId, lockStatus);
    }

    @Transactional
    public LockSuccessResponseDTO lockPlan(int planId, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);
        // 3분 동안 락을 획득
        boolean success = planLockUtil.acquirePlanLock(planId, user.getUserId(), LOCK_TIMEOUT);

        return new LockSuccessResponseDTO(success);
    }

    @Transactional
    public LockSuccessResponseDTO releaseLock(int planId, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // 락 해제
        boolean success = planLockUtil.releasePlanLock(planId);

        return new LockSuccessResponseDTO(success);
    }

    @Transactional
    public PlanResponseDTO updatePlanSchedule(int planId, PlanScheduleRequestDTO scheduleRequestDTO, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // 락 체크
        Integer lockUserId = planLockUtil.checkPlanLock(planId);
        boolean lockStatus = lockUserId != null;
        if(lockStatus) {
            throw new LockedPlanException("해당 계획은 다른 사용자에 의해 Lock 되어 있습니다. planId: " + planId);
        }

        // 일정 업데이트
        planDAO.updatePlanSchedule(planId, scheduleRequestDTO.getStartDate(), scheduleRequestDTO.getEndDate());
        planDAO.deleteRoutesExceedingDayIndex(planId);
        // TODO: stella 업데이트
        return getPlanDetail(planId, user); // 업데이트된 계획을 가져오기 위해 다시 호출
    }

    @Transactional
    public PlanResponseDTO addAttraction(int planId, RouteInsertRequestDTO routeInsertRequestDTO, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // day index 체크
        PlanDTO plan = planDAO.getPlanById(planId, user.getUserId());
        long dateDiff = ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate());
        if (routeInsertRequestDTO.getDayIndex() < 1 || routeInsertRequestDTO.getDayIndex() > dateDiff + 1) {
            throw new IllegalDayIndexException("유효하지 않은 day index입니다. day index: " + routeInsertRequestDTO.getDayIndex());
        }

        // attractionId 체크
        // TODO: attractionId 유효성 체크

        // order 구하기
        int insertOrder = 1;
        for (RouteDTO route : plan.getRoutes()) {
            if (route.getDayIndex() == routeInsertRequestDTO.getDayIndex()) {
                insertOrder = Math.max(insertOrder, route.getOrder() + 1);
            }
        }

        // 일정 추가
        planDAO.insertRoute(
                planId,
                routeInsertRequestDTO.getAttractionId(),
                routeInsertRequestDTO.getDayIndex(),
                insertOrder,
                routeInsertRequestDTO.getVisitTime(),
                routeInsertRequestDTO.getMemo()
        );
        // TODO: stella 업데이트
        return getPlanDetail(planId, user); // 업데이트된 계획을 가져오기 위해 다시 호출
    }

    @Transactional
    public PlanResponseDTO updatePlanRoutes(int planId, RoutesUpdateRequestDTO routesUpdateRequestDTO, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // 락 체크
        Integer lockUserId = planLockUtil.checkPlanLock(planId);
        if(lockUserId != null && lockUserId != user.getUserId()) {
            throw new LockedPlanException("해당 계획은 다른 사용자에 의해 Lock 되어 있습니다. planId: " + planId);
        }
        if(lockUserId == null){
            throw new UnlockedException("해당 계획은 Lock 되어 있지 않습니다. planId: " + planId);
        }

        // 혹시 모를 상황에 대비해 lock 연장
        planLockUtil.acquirePlanLock(planId, user.getUserId(), LOCK_TIMEOUT);

        // 일정 범위 체크
        PlanDTO plan = planDAO.getPlanById(planId, user.getUserId());
        long dateDiff = ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate());

        // 루트 업데이트
        List<RouteDTO> updatingRoutes = new ArrayList<>();
        List<RouteDTO> deletingRoutes = new ArrayList<>();
        for (RoutesUpdateRequestDTO.RouteDTO route : routesUpdateRequestDTO.getRoutes()) {
            if(route.isDeleted()){
                RouteDTO routeDTO = RouteDTO.builder()
                        .routeId(route.getRouteId())
                        .build();
                deletingRoutes.add(routeDTO);
            }else{
                if (route.getDayIndex() < 1 || route.getDayIndex() > dateDiff + 1) {
                    throw new IllegalDayIndexException("유효하지 않은 day index입니다. day index: " + route.getDayIndex());
                }
                RouteDTO routeDTO = RouteDTO.builder()
                        .routeId(route.getRouteId())
                        .dayIndex(route.getDayIndex())
                        .order(route.getOrder())
                        .build();
                updatingRoutes.add(routeDTO);
            }
        }
        planDAO.updateRoutes(updatingRoutes);
        planDAO.deleteRoutes(deletingRoutes);
        // TODO: stella 업데이트
        planLockUtil.releasePlanLock(planId);
        return getPlanDetail(planId, user); // 업데이트된 계획을 가져오기 위해 다시 호출
    }

    @Transactional
    public PlanResponseDTO addPlan(PlanRequestDTO planRequestDTO, JwtUserInfo user) {

        String stella = "0.0"; // TODO: stella 계산 로직 추가
        // 계획 추가
        PlanDTO planDTO = PlanDTO.builder()
                .title(planRequestDTO.getTitle())
                .description(planRequestDTO.getDescription())
                .stella(stella)
                .startDate(planRequestDTO.getStartDate())
                .endDate(planRequestDTO.getEndDate())
                .isPublic(planRequestDTO.isPublic())
                .build();
        planDAO.insertPlan(planDTO);

        // 태그 추가 (두 단계로 분리)
        if (planRequestDTO.getTags() != null && !planRequestDTO.getTags().isEmpty()) {
            planDAO.insertTags(planRequestDTO.getTags());
            planDAO.linkTagsToPlan(planDTO.getPlanId(), planRequestDTO.getTags());
        }

        // 작성자 추가
        planDAO.insertPlanWriter(planDTO.getPlanId(), user.getUserId());

        return getPlanDetail(planDTO.getPlanId(), user); // 생성된 계획을 가져오기 위해 다시 호출
    }

    @Transactional
    public Boolean leavePlan(int planId, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // 계획에서 나가기
        planDAO.deletePlanWriter(planId, user.getUserId());

        // 작성자가 없으면 계획 삭제
        if (planDAO.getPlanWritersCount(planId) == 0) {
            planDAO.deletePlan(planId);
            return true;
        }
        return true;
    }

    @Transactional
    public Boolean invitePlan(int planId, String email, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // 이메일로 사용자 찾기
        UserDTO invitedUser = userDAO.getUserByEmail(email);
        if (invitedUser == null) {
            throw new UserNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다. email: " + email);
        }

        // 이미 초대된 사용자 체크
        if (planDAO.checkPlanWriter(planId, invitedUser.getUserId())) {
            throw new DuplicatedWriterException("이미 초대된 사용자입니다. email: " + email);
        }

        // 초대하기
        planDAO.insertPlanWriter(planId, invitedUser.getUserId());
        return true;
    }

    @Transactional
    public PlanResponseDTO updatePlan(int planId, BasicPlanRequestDTO basicPlanRequestDTO, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        planDAO.updateBasicPlanInfo(
                planId,
                basicPlanRequestDTO.getTitle(),
                basicPlanRequestDTO.getDescription(),
                basicPlanRequestDTO.isPublic()
        );

        return getPlanDetail(planId, user); // 생성된 계획을 가져오기 위해 다시 호출
    }

    @Transactional
    public RouteResponseDTO updateRoute(int planId, int routeId, RouteUpdateRequestDTO routeUpdateRequestDTO, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);
        RouteDTO route = planDAO.getRouteByRouteId(routeId);
        if (route == null) {
            throw new RouteNotFoundException("해당 ID의 루트를 찾을 수 없습니다. routeId: " + routeId);
        }
        if(route.getPlanId() != planId){
            throw new RouteNotInPlanException("해당 루트는 계획에 속하지 않습니다. planId: " + planId + ", routeId: " + routeId);
        }

        // 일정 업데이트
        planDAO.updateRouteInfo(
                routeId,
                routeUpdateRequestDTO.getVisitTime(),
                routeUpdateRequestDTO.getMemo()
        );

        return convertRouteToResponse(planDAO.getRouteByRouteId(routeId));
    }

    private void checkPlanAuthority(int planId, JwtUserInfo user) throws PlanNotFoundException, UnauthorizedPlanAccessException{
        // 계획 유무 체크
        PlanDTO planDTO = planDAO.getPlanById(planId, user.getUserId());
        if(planDTO == null) {
            throw new PlanNotFoundException("해당 ID의 계획을 찾을 수 없습니다. planId: " + planId);
        }

        // 권한 체크
        if (planDTO.getWriters().stream().allMatch(writer -> writer.getUserId() != user.getUserId())) {
            throw new UnauthorizedPlanAccessException("해당 계획에 대한 접근 권한이 없습니다. planId: " + planId);
        }
    }

    private List<TagResponseDTO> convertTagsToResponse(List<TagDTO> tags) {
        List<TagResponseDTO> tagResponseDTOList = new ArrayList<>();
        for (TagDTO tag : tags) {
            TagResponseDTO tagResponseDTO = TagResponseDTO.builder()
                    .tagId(tag.getTagId())
                    .name(tag.getName())
                    .build();
            tagResponseDTOList.add(tagResponseDTO);
        }
        return tagResponseDTOList;
    }

    private List<WriterResponseDTO> convertWritersToResponse(List<UserDTO> writers) {
        List<WriterResponseDTO> writerResponseDTOList = new ArrayList<>();
        for (UserDTO writer : writers) {
            WriterResponseDTO writerResponseDTO = WriterResponseDTO.builder()
                    .userId(writer.getUserId())
                    .name(writer.getName())
                    .build();
            writerResponseDTOList.add(writerResponseDTO);
        }
        return writerResponseDTOList;
    }

    private RouteResponseDTO convertRouteToResponse(RouteDTO routeDTO) {
        return RouteResponseDTO.builder()
                .routeId(routeDTO.getRouteId())
                .attractionId(routeDTO.getAttractionId())
                .order(routeDTO.getOrder())
                .name(routeDTO.getAttraction().getTitle())
                .image(routeDTO.getAttraction().getFirstImage1())
                .address(routeDTO.getAttraction().getAddr1())
                .contentTypeId(routeDTO.getAttraction().getContentTypeId())
                .likeCount(routeDTO.getAttraction().getLikeCount())
                .rating(routeDTO.getAttraction().getRating())
                .latitude(routeDTO.getAttraction().getLatitude())
                .longitude(routeDTO.getAttraction().getLongitude())
                .visitTime(routeDTO.getVisitTime())
                .memo(routeDTO.getMemo())
                .build();
    }

    private Map<LocalDate, List<RouteResponseDTO>> convertRoutesToResponse(List<RouteDTO> routes, LocalDate startDate) {
        Map<LocalDate, List<RouteResponseDTO>> routeResponseDTOMap = new HashMap<>();
        for (RouteDTO route : routes) {
            // RouteResponseDTO 생성
            RouteResponseDTO routeResponseDTO = convertRouteToResponse(route);
            // 날짜에 맞춰서 리스트에 추가
            LocalDate visitDate = startDate.plusDays(route.getDayIndex() - 1);
            routeResponseDTOMap.computeIfAbsent(visitDate, k -> new ArrayList<>()).add(routeResponseDTO);
        }
        // order대로 정렬
        routeResponseDTOMap.keySet().stream()
                .sorted()
                .forEach(date -> routeResponseDTOMap.get(date).sort(Comparator.comparingInt(RouteResponseDTO::getOrder)));
        return routeResponseDTOMap;
    }
}
