package com.ssafy.stella_trip.plan.service;

import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.response.PlanResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.TagResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.WriterResponseDTO;
import com.ssafy.stella_trip.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            String sort
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
        List<PlanDTO> planDTOList = planDAO.getPlansByCondition(offset, size, search, userName, duration, sort);
        List<PlanResponseDTO> planResponseDTOList = new ArrayList<>();
        for (PlanDTO planDTO : planDTOList) {
            // 태그 리스트
            List<TagResponseDTO> tagResponseDTOList = new ArrayList<>();
            for (TagDTO tag : planDTO.getTags()) {
                TagResponseDTO tagResponseDTO = TagResponseDTO.builder()
                        .tagId(tag.getTagId())
                        .name(tag.getName())
                        .build();
                tagResponseDTOList.add(tagResponseDTO);
            }

            // 작성자 리스트
            List<WriterResponseDTO> writerResponseDTOList = new ArrayList<>();
            for(UserDTO writer : planDTO.getWriters()) {
                WriterResponseDTO writerResponseDTO = WriterResponseDTO.builder()
                        .userId(writer.getUserId())
                        .name(writer.getName())
                        .build();
                writerResponseDTOList.add(writerResponseDTO);
            }

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


}
