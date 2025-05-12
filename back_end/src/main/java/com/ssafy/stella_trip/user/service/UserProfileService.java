package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.attraction.dto.AttractionWithReviewsDTO;
import com.ssafy.stella_trip.attraction.dto.response.AttractionResponseDTO;
import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.dao.attraction.AttractionDAO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.response.PlanResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.TagResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.WriterResponseDTO;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.user.dto.UserDTO;
import com.ssafy.stella_trip.user.dto.UserProfileDTO;
import com.ssafy.stella_trip.user.dto.request.MyProfileUpdateRequestDTO;
import com.ssafy.stella_trip.user.dto.response.FollowResponseDTO;
import com.ssafy.stella_trip.user.dto.response.MyProfileResponseDTO;
import com.ssafy.stella_trip.user.dto.response.UserProfileResponseDTO;
import com.ssafy.stella_trip.user.exception.ProfileNotFoundException;
import com.ssafy.stella_trip.user.exception.ProfileUpdateFailureException;
import com.ssafy.stella_trip.user.exception.PasswordUpdateFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserDAO userDAO;
    private final PlanDAO planDAO;
    private final AttractionDAO attractionDAO;
    private final PasswordEncoder passwordEncoder;

    private int getCurrentAuthenticatedUserId() {
        //TODO: 자신의 userId 획득
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(principal instanceof JwtUserInfo)) {
            throw new ProfileNotFoundException("인증된 사용자의 정보가 아닙니다.");
        }
        JwtUserInfo userInfo = (JwtUserInfo) principal;
        return userInfo.getUserId();
    }


    /**
     * 다른 사용자의 프로필 정보 조회
     * @param userId 조회할 사용자 id
     * @return 사용자 프로필 정보
     * @throws ProfileNotFoundException 프로필을 찾을 수 없는 경우
     */
    public UserProfileResponseDTO getUserProfile(int userId) {
        UserProfileDTO profile = userDAO.getUserProfile(userId);

        if (profile == null) {
            throw new ProfileNotFoundException("사용자 ID " + userId + "에 해당하는 프로필을 찾을 수 없습니다.");
        }

        UserProfileResponseDTO userProfileResponseDTO = UserProfileResponseDTO.builder()
                .name(profile.getName())
                .description(profile.getDescription())
                .image(profile.getImage())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .build();

        return userProfileResponseDTO;
    }

    /**
     * 자신의 프로필 정보 조회
     * @return 자신의 프로필 정보
     * @throws ProfileNotFoundException 프로필을 찾을 수 없는 경우
     */
    public MyProfileResponseDTO getMyProfile() {
        int userId = getCurrentAuthenticatedUserId();

        UserProfileDTO profile = userDAO.getUserProfile(userId);

        if (profile == null) {
            throw new ProfileNotFoundException("자신의 프로필을 찾을 수 없습니다.");
        }

        MyProfileResponseDTO myProfileResponseDTO = MyProfileResponseDTO.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .description(profile.getDescription())
                .image(profile.getImage())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .build();

        return myProfileResponseDTO;
    }

    /**
     * 자신의 프로필 정보 업데이트
     * @param myProfileUpdateRequestDTO 업데이트할 프로필 정보
     * @return 업데이트된 자신의 프로필 정보
     * @throws ProfileUpdateFailureException 프로필 업데이트 실패 시
     * @throws ProfileNotFoundException 업데이트 후 프로필 조회 실패 시
     */
    public MyProfileResponseDTO updateMyProfile(MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) {
        int userId = getCurrentAuthenticatedUserId();

        // 업데이트 수행 및 결과 확인
        int affectedRows = userDAO.updateMyProfileByUserId(
                UserDTO.builder()
                        .userId(userId)
                        .name(myProfileUpdateRequestDTO.getName())
                        .description(myProfileUpdateRequestDTO.getDescription())
                        .image(myProfileUpdateRequestDTO.getImage())
                        .build()
        );
        if (affectedRows == 0) {
            throw new ProfileUpdateFailureException("프로필 업데이트에 실패했습니다.");
        }

        // 업데이트된 프로필 조회
        UserProfileDTO profile = userDAO.getUserProfile(userId);
        if (profile == null) {
            throw new ProfileNotFoundException("업데이트된 프로필을 찾을 수 없습니다.");
        }

        MyProfileResponseDTO myProfileResponseDTO = MyProfileResponseDTO.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .description(profile.getDescription())
                .image(profile.getImage())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .build();

        return myProfileResponseDTO;
    }

    /**
     * 자신의 비밀번호 업데이트
     * @param password 새로운 비밀번호
     * @return 비밀번호 변경 성공, 실패 여부
     * @throws PasswordUpdateFailureException 비밀번호 업데이트 실패 시
     */
    public boolean updatePassword(String password){
        int userId = getCurrentAuthenticatedUserId();

        String encodedPassword = passwordEncoder.encode(password);
        int affectedRows = userDAO.updatePasswordByUserId(
                UserDTO.builder()
                        .userId(userId)
                        .password(encodedPassword)
                        .build()
        );
        if (affectedRows == 0) {
            throw new PasswordUpdateFailureException("비밀번호 업데이트에 실패했습니다.");
        }

        return true;
    }

    /**
     * 사용자의 여행 계획 리스트 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 여행 계획 리스트
     */
    public PageDTO<PlanResponseDTO> getUserPlans(int page, int size) {
        int userId = getCurrentAuthenticatedUserId();
        int totalPlans = planDAO.countUserPlansByUserId(userId);

        PageInfo pageInfo = calculatePageInfo(totalPlans, page, size);

        List<PlanDTO> userPlans = planDAO.getUserPlansByUserId(userId, pageInfo.offset, size);

        List<PlanResponseDTO> planResponses = new ArrayList<>();
        for (PlanDTO planDTO : userPlans) {
            PlanResponseDTO planResponseDTO = PlanResponseDTO.builder()
                    .planId(planDTO.getPlanId())
                    .title(planDTO.getTitle())
                    .description(planDTO.getDescription())
                    .stella(planDTO.getStella())
                    .startDate(planDTO.getStartDate())
                    .endDate(planDTO.getEndDate())
                    .likeCount(planDTO.getLikeCount())
                    .isPublic(planDTO.isPublic())
                    .planWriters(convertWritersToResponse(planDTO.getWriters()))
                    .tags(convertTagsToResponse(planDTO.getTags()))
                    .build();
            planResponses.add(planResponseDTO);
        }

        return PageDTO.<PlanResponseDTO>builder()
                .content(planResponses)
                .totalElements(totalPlans)
                .totalPages(pageInfo.totalPages)
                .page(page)
                .size(size)
                .isFirst(pageInfo.isFirst)
                .isLast(pageInfo.isLast)
                .hasNext(pageInfo.hasNext)
                .build();
    }

    /**
     * 사용자의 좋아요한 여행 계획 리스트 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 좋아요한 여행 계획 리스트
     */
    public PageDTO<PlanResponseDTO> getLikedPlans(int page, int size) {
        int userId = getCurrentAuthenticatedUserId();
        int totalPlans = planDAO.countLikedPlansByUserId(userId);

        PageInfo pageInfo = calculatePageInfo(totalPlans, page, size);

        List<PlanDTO> likedPlans = planDAO.getLikedPlansByUserId(userId, pageInfo.offset, size);

        List<PlanResponseDTO> planResponses = new ArrayList<>();
        for (PlanDTO planDTO : likedPlans) {
            PlanResponseDTO planResponseDTO = PlanResponseDTO.builder()
                    .planId(planDTO.getPlanId())
                    .title(planDTO.getTitle())
                    .description(planDTO.getDescription())
                    .stella(planDTO.getStella())
                    .startDate(planDTO.getStartDate())
                    .endDate(planDTO.getEndDate())
                    .likeCount(planDTO.getLikeCount())
                    .isPublic(planDTO.isPublic())
                    .planWriters(convertWritersToResponse(planDTO.getWriters()))
                    .tags(convertTagsToResponse(planDTO.getTags()))
                    .build();
            planResponses.add(planResponseDTO);
        }

        return PageDTO.<PlanResponseDTO>builder()
                .content(planResponses)
                .totalElements(totalPlans)
                .totalPages(pageInfo.totalPages)
                .page(page)
                .size(size)
                .isFirst(pageInfo.isFirst)
                .isLast(pageInfo.isLast)
                .hasNext(pageInfo.hasNext)
                .build();
    }

    /**
     * 특정 사용자의 팔로잉 목록 조회
     * @param userId 조회할 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 팔로잉 목록
     */
    public PageDTO<FollowResponseDTO> getFollowings(int userId, int page, int size) {
        int totalFollowings = userDAO.countFollowingsByUserId(userId);

        PageInfo pageInfo = calculatePageInfo(totalFollowings, page, size);

        List<UserDTO> followings = userDAO.getFollowingsByUserId(userId, pageInfo.offset, size);

        List<FollowResponseDTO> followResponses = followings.stream()
                .map(this::convertToFollowResponseDTO)
                .toList();

        return PageDTO.<FollowResponseDTO>builder()
                .content(followResponses)
                .totalElements(totalFollowings)
                .totalPages(pageInfo.totalPages)
                .page(page)
                .size(size)
                .isFirst(pageInfo.isFirst)
                .isLast(pageInfo.isLast)
                .hasNext(pageInfo.hasNext)
                .build();
    }

    /**
     * 특정 사용자의 팔로워 목록 조회
     * @param userId 조회할 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 팔로워 목록
     */
    public PageDTO<FollowResponseDTO> getFollowers(int userId, int page, int size) {
        int totalFollowers = userDAO.countFollowersByUserId(userId);

        PageInfo pageInfo = calculatePageInfo(totalFollowers, page, size);

        List<UserDTO> followers = userDAO.getFollowersByUserId(userId, pageInfo.offset, size);

        List<FollowResponseDTO> followResponses = followers.stream()
                .map(this::convertToFollowResponseDTO)
                .toList();

        return PageDTO.<FollowResponseDTO>builder()
                .content(followResponses)
                .totalElements(totalFollowers)
                .totalPages(pageInfo.totalPages)
                .page(page)
                .size(size)
                .isFirst(pageInfo.isFirst)
                .isLast(pageInfo.isLast)
                .hasNext(pageInfo.hasNext)
                .build();
    }

    /**
     * 사용자가 좋아요한 관광지 목록 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 좋아요한 관광지 목록
     */
    public PageDTO<AttractionResponseDTO> getLikedAttractions(int page, int size) {
        int userId = getCurrentAuthenticatedUserId();
        int totalAttractions = attractionDAO.countLikedAttractionsByUserId(userId);

        PageInfo pageInfo = calculatePageInfo(totalAttractions, page, size);

        List<AttractionWithReviewsDTO> attractionsWithReviews =
                attractionDAO.getLikedAttractionsWithReviews(userId, pageInfo.offset, size);

        List<AttractionResponseDTO> attractionResponses = attractionsWithReviews.stream()
                .map(attraction -> AttractionResponseDTO.builder()
                        .attractionId(attraction.getAttractionId())
                        .name(attraction.getTitle())
                        .image(attraction.getFirstImage1())
                        .address(attraction.getAddr1() + (attraction.getAddr2() != null ? " " + attraction.getAddr2() : ""))
                        .contentType(attraction.getContentTypeId())
                        .like(attraction.getLikeCount())
                        .rating(attraction.getRating())
                        .latitude(attraction.getLatitude())
                        .longitude(attraction.getLongitude())
                        .review(attraction.getReviews())
                        .build())
                .collect(Collectors.toList());

        return PageDTO.<AttractionResponseDTO>builder()
                .content(attractionResponses)
                .totalElements(totalAttractions)
                .totalPages(pageInfo.totalPages)
                .page(page)
                .size(size)
                .isFirst(pageInfo.isFirst)
                .isLast(pageInfo.isLast)
                .hasNext(pageInfo.hasNext)
                .build();
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

    /**
     * UserDTO를 FollowResponseDTO로 변환
     */
    private FollowResponseDTO convertToFollowResponseDTO(UserDTO userDTO) {
        return new FollowResponseDTO(
                userDTO.getUserId(),
                userDTO.getName(),
                userDTO.getImage(),
                userDTO.getDescription()
        );
    }

    /**
     * 페이징 정보를 계산
     */
    private PageInfo calculatePageInfo(int totalElements, int page, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        int offset = (page - 1) * size;
        boolean hasNext = page < totalPages;
        boolean isFirst = page == 1;
        boolean isLast = totalPages == 0 || page >= totalPages;

        return new PageInfo(totalPages, offset, hasNext, isFirst, isLast);
    }

    /**
     * 페이징 정보를 담는 클래스
     */
    private static class PageInfo {
        final int totalPages;
        final int offset;
        final boolean hasNext;
        final boolean isFirst;
        final boolean isLast;

        PageInfo(int totalPages, int offset, boolean hasNext, boolean isFirst, boolean isLast) {
            this.totalPages = totalPages;
            this.offset = offset;
            this.hasNext = hasNext;
            this.isFirst = isFirst;
            this.isLast = isLast;
        }
    }

}