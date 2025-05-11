package com.ssafy.stella_trip.plan.service;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.request.AttractionPostRequestDTO;
import com.ssafy.stella_trip.plan.dto.request.PlanScheduleRequestDTO;
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
        return getPlanDetail(planId, user); // 업데이트된 계획을 가져오기 위해 다시 호출
    }

    public PlanResponseDTO addAttraction(int planId, AttractionPostRequestDTO attractionPostRequestDTO, JwtUserInfo user) {
        // 권한 체크
        checkPlanAuthority(planId, user);

        // day index 체크
        PlanDTO plan = planDAO.getPlanById(planId, user.getUserId());
        long dateDiff = ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate());
        if (attractionPostRequestDTO.getDayIndex() < 1 || attractionPostRequestDTO.getDayIndex() > dateDiff + 1) {
            throw new IllegalDayIndexException("유효하지 않은 day index입니다. day index: " + attractionPostRequestDTO.getDayIndex());
        }

        // attractionId 체크
        // TODO: attractionId 유효성 체크

        // order 구하기
        int insertOrder = 1;
        for (RouteDTO route : plan.getRoutes()) {
            if (route.getDayIndex() == attractionPostRequestDTO.getDayIndex()) {
                insertOrder = Math.max(insertOrder, route.getOrder() + 1);
            }
        }

        // 일정 추가
        planDAO.insertRoute(
                planId,
                attractionPostRequestDTO.getAttractionId(),
                attractionPostRequestDTO.getDayIndex(),
                insertOrder,
                attractionPostRequestDTO.getVisitTime(),
                attractionPostRequestDTO.getMemo()
        );
        return getPlanDetail(planId, user); // 업데이트된 계획을 가져오기 위해 다시 호출
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

    private Map<LocalDate, List<RouteResponseDTO>> convertRoutesToResponse(List<RouteDTO> routes, LocalDate startDate) {
        Map<LocalDate, List<RouteResponseDTO>> routeResponseDTOMap = new HashMap<>();
        for (RouteDTO route : routes) {
            RouteResponseDTO routeResponseDTO = RouteResponseDTO.builder()
                    .routeId(route.getRouteId())
                    .attractionId(route.getAttractionId())
                    .order(route.getOrder())
                    .name(route.getAttraction().getTitle())
                    .image(route.getAttraction().getFirstImage1())
                    .address(route.getAttraction().getAddr1())
                    .contentTypeId(route.getAttraction().getContentTypeId())
                    .likeCount(route.getAttraction().getLikeCount())
                    .rating(route.getAttraction().getRating())
                    .latitude(route.getAttraction().getLatitude())
                    .longitude(route.getAttraction().getLongitude())
                    .visitTime(route.getVisitTime())
                    .memo(route.getMemo())
                    .build();
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
