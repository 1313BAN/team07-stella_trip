import { ATTRACTION } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse, CommonSuccessBody } from '../../types';
import type {
  Attraction,
  AttractionsParams,
  AttractionDetail,
  Sigungu,
  Review,
  ReviewRequest,
  FeaturedAttraction,
} from './types';
import type { FeaturedTags } from '../plan';

/**
 * query parameter를 바탕으로 여행지 리스트 조회
 * @returns 여행지 리스트
 */
export const getAttractions = async (
  params?: AttractionsParams
): Promise<PagenationResponse<Attraction>> => {
  const response = await api.get<ApiResponse<PagenationResponse<Attraction>>>(
    ATTRACTION.ATTRACTIONS,
    {
      params,
      paramsSerializer: {
        indexes: null, // 이렇게 설정하면 contentTypeIdList[]가 아니라 contentTypeIdList로 직렬화됩니다
      },
    }
  );

  return response.data.body;
};

/**
 * attractionId 를 바탕으로 여행지 상세 정보 조회
 * @param attractionId 여행지 ID
 * @returns 여행지 상세 정보
 */
export const getAttractionDetail = async (attractionId: number): Promise<AttractionDetail> => {
  const response = await api.get<ApiResponse<AttractionDetail>>(ATTRACTION.DETAIL(attractionId));

  return response.data.body;
};

/**
 * 시군구 조회 API
 * @returns 시군구 리스트
 */
export const getSigunguList = async (): Promise<Sigungu> => {
  const response = await api.get<ApiResponse<Sigungu>>(ATTRACTION.SIGUNGU);

  return response.data.body;
};

/**
 * 여행지 좋아요 API
 * @param attractionId 여행지 ID
 * @returns 좋아요 여부
 */
export const postAttractionLike = async (attractionId: number): Promise<boolean> => {
  const response = await api.post<ApiResponse<CommonSuccessBody>>(ATTRACTION.LIKE(attractionId));

  return response.data.body.success;
};

/**
 * 여행지 좋아요 취소 API
 * @param attractionId 여행지 ID
 * @returns 좋아요 취소 여부
 */
export const deleteAttractionLike = async (attractionId: number): Promise<boolean> => {
  const response = await api.delete<ApiResponse<CommonSuccessBody>>(ATTRACTION.LIKE(attractionId));

  return response.data.body.success;
};

/**
 * 여행지 리뷰 조회 API
 * @param attractionId 여행지 ID
 * @returns 여행지 리뷰 리스트
 */

export const getAttractionReview = async (
  attractionId: number
): Promise<PagenationResponse<Review>> => {
  const response = await api.get<ApiResponse<PagenationResponse<Review>>>(
    ATTRACTION.REVIEW(attractionId)
  );

  return response.data.body;
};

/**
 * 여행지 리뷰 작성 API
 * @param attractionId 여행지 ID
 * @param review 여행지 리뷰
 * @returns 여행지 리뷰 작성 여부
 */
export const postAttractionReview = async (
  attractionId: number,
  review: ReviewRequest
): Promise<boolean> => {
  const response = await api.post<ApiResponse<CommonSuccessBody>>(
    ATTRACTION.REVIEW(attractionId),
    review
  );

  return response.data.body.success;
};

/**
 * 여행지 리뷰 수정 API
 * @param attractionId 여행지 ID
 * @param reviewId 여행지 리뷰 ID
 * @param review 여행지 리뷰
 * @returns 여행지 리뷰 수정 여부
 */
export const postReviewLike = async (attractionId: number, reviewId: number): Promise<boolean> => {
  const response = await api.post<ApiResponse<CommonSuccessBody>>(
    ATTRACTION.REVIEW_LIKE(attractionId, reviewId)
  );

  return response.data.body.success;
};

/**
 * 여행지 리뷰 수정 API
 * @param attractionId 여행지 ID
 * @param reviewId 여행지 리뷰 ID
 * @param review 여행지 리뷰
 * @returns 여행지 리뷰 수정 여부
 */
export const deleteReviewLike = async (
  attractionId: number,
  reviewId: number
): Promise<boolean> => {
  const response = await api.delete<ApiResponse<CommonSuccessBody>>(
    ATTRACTION.REVIEW_LIKE(attractionId, reviewId)
  );

  return response.data.body.success;
};

/**
 * 인기 많은 여행 계획 태그 리스트 조회
 * @returns 여행 계획 태그 리스트
 */
export const getPopularTags = async (): Promise<FeaturedTags> => {
  const response = await api.get<ApiResponse<FeaturedTags>>(ATTRACTION.FEATURED_TAGS);

  return response.data.body;
};

export const getFeaturedAttractions = async (
  contentTypeId: number
): Promise<FeaturedAttraction> => {
  const response = await api.get<ApiResponse<FeaturedAttraction>>(
    ATTRACTION.FEATURED_ATTRACTIONS(contentTypeId)
  );

  return response.data.body;
};
