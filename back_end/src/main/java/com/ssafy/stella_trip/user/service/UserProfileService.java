package com.ssafy.stella_trip.user.service;

import com.ssafy.stella_trip.attraction.dto.AttractionWithReviewsDTO;
import com.ssafy.stella_trip.attraction.dto.response.AttractionResponseDTO;
import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.util.PaginationUtils;
import com.ssafy.stella_trip.dao.attraction.AttractionDAO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.dao.user.UserDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import com.ssafy.stella_trip.plan.dto.response.PlanResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.TagResponseDTO;
import com.ssafy.stella_trip.plan.dto.response.WriterResponseDTO;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.user.dto.FollowUserDTO;
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

        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> planDAO.countUserPlansByUserId(userId),
                (offset, pageSize) -> planDAO.getUserPlansByUserId(userId, offset, pageSize),
                this::convertPlanToResponseDTO
        );
    }

    /**
     * 사용자의 좋아요한 여행 계획 리스트 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 좋아요한 여행 계획 리스트
     */
    public PageDTO<PlanResponseDTO> getLikedPlans(int page, int size) {
        int userId = getCurrentAuthenticatedUserId();

        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> planDAO.countLikedPlansByUserId(userId),
                (offset, pageSize) -> planDAO.getLikedPlansByUserId(userId, offset, pageSize),
                this::convertPlanToResponseDTO
        );
    }

    /**
     * 특정 사용자의 팔로잉 목록 조회
     * @param userId 조회할 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 팔로잉 목록
     */
    public PageDTO<FollowResponseDTO> getFollowings(int userId, int page, int size) {
        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> userDAO.countFollowingsByUserId(userId),
                (offset, pageSize) -> userDAO.getFollowingsByUserId(userId, offset, pageSize),
                this::convertToFollowResponseDTO
        );
    }

    /**
     * 특정 사용자의 팔로워 목록 조회
     * @param userId 조회할 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 팔로워 목록
     */
    public PageDTO<FollowResponseDTO> getFollowers(int userId, int page, int size) {
        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> userDAO.countFollowersByUserId(userId),
                (offset, pageSize) -> userDAO.getFollowersByUserId(userId, offset, pageSize),
                this::convertToFollowResponseDTO
        );
    }

    /**
     * 사용자가 좋아요한 관광지 목록 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 좋아요한 관광지 목록
     */
    public PageDTO<AttractionResponseDTO> getLikedAttractions(int page, int size) {
        int userId = getCurrentAuthenticatedUserId();

        return PaginationUtils.getPagedResult(
                page,
                size,
                () -> attractionDAO.countLikedAttractionsByUserId(userId),
                (offset, pageSize) -> attractionDAO.getLikedAttractionsWithReviews(userId, offset, pageSize),
                this::convertAttractionToResponseDTO
        );
    }

    /**
     * PlanDTO를 PlanResponseDTO로 변환
     * @param planDTO planDTO
     * @return PlanResponseDTO
     */
    private PlanResponseDTO convertPlanToResponseDTO(PlanDTO planDTO) {
        return PlanResponseDTO.builder()
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
    }

    /**
     * AttractionDTO를 AttractionResponseDTO로 변환
     * @param attraction attractionDTO
     * @return AttractionResponseDTO
     */
    private AttractionResponseDTO convertAttractionToResponseDTO(AttractionWithReviewsDTO attraction) {
        return AttractionResponseDTO.builder()
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
    private FollowResponseDTO convertToFollowResponseDTO(FollowUserDTO FollowUserDTO) {
        return new FollowResponseDTO(
                FollowUserDTO.getUserId(),
                FollowUserDTO.getName(),
                FollowUserDTO.getImage(),
                FollowUserDTO.getDescription(),
                FollowUserDTO.getFollowsCreatedAt()
        );
    }
}