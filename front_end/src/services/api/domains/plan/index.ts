import { PLAN } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse } from '../../types';
import type {
  Plan,
  PlansParams,
  Tag,
  PlanDetail,
  CreatePlanRequest,
  CreatedPlanResponse,
  PlanAttractionRequest,
} from './types';

/**
 * query parameter를 바탕으로 여행 계획 리스트 조회
 * @returns 여행 계획 리스트
 */
export const getPlans = async (params?: PlansParams): Promise<PagenationResponse<Plan>> => {
  const response = await api.get<ApiResponse<PagenationResponse<Plan>>>(PLAN.PLANS, {
    params,
  });

  return response.data.body;
};

/**
 * 인기 많은 여행 계획 태그 리스트 조회
 * @returns 여행 계획 태그 리스트
 */
export const getPopularTags = async (size?: number): Promise<Tag[]> => {
  const response = await api.get<ApiResponse<Tag[]>>(PLAN.TAGS, { params: { size } });

  return response.data.body;
};

/**
 * 여행 계획 상세 정보 조회
 * @param planId planId
 * @returns 여행 계획 상세 정보
 */
export const getPlanDetail = async (planId: number): Promise<PlanDetail> => {
  const response = await api.get<ApiResponse<PlanDetail>>(PLAN.DETAIL(planId));

  return response.data.body;
};

/**
 * 여행 계획 좋아요
 * @param planId planId
 * @returns
 */
export const postPlanLike = async (planId: number): Promise<void> => {
  await api.post<ApiResponse<void>>(PLAN.LIKE(planId));
};

/**
 * 여행 계획 좋아요 취소
 * @param planId planId
 * @returns
 */
export const deletePlanLike = async (planId: number): Promise<void> => {
  await api.delete<ApiResponse<void>>(PLAN.LIKE(planId));
};

/**
 * 여행 계획 추가
 * @param data 여행 계획 데이터
 * @returns 생성된 여행 계획 정보
 */
export const createPlan = async (data: CreatePlanRequest): Promise<CreatedPlanResponse> => {
  const response = await api.post<ApiResponse<CreatedPlanResponse>>(PLAN.PLANS, data);

  return response.data.body;
};

/**
 * 여행 계획에 명소 추가
 * @param planId planId
 * @param data 명소 추가 데이터
 * @returns 여행 계획 상세 정보
 */
export const addAttractionToDate = async (
  planId: number,
  data: PlanAttractionRequest
): Promise<PlanDetail> => {
  const response = await api.post<ApiResponse<PlanDetail>>(PLAN.ADD_ATTRACTIONS(planId), data);

  return response.data.body;
};

export * from './types';
