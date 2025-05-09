package com.ssafy.stella_trip.plan.service;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.response.*;
import com.ssafy.stella_trip.plan.exception.PlanNotFoundException;
import com.ssafy.stella_trip.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanDAO planDAO;

    public PageDTO<PlanResponseDTO> searchPlanByCondition(
            int page,
            int size,
            String search,
            String userName,
            int duration,
            String sort,
            int currentUserId
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
        List<PlanDTO> planDTOList = planDAO.getPlansByCondition(offset, size, search, userName, duration, sort, currentUserId);
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

    public PlanDetailResponseDTO getPlanDetail(int planId, int currentUserId) {
        // PlanDTO 가져오기
        PlanDTO planDTO = planDAO.getPlanById(planId, currentUserId);
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
