import { PLAN } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse } from '../../types';
import type { Plan, PlansParams, Tag, PlanDetail } from './types';

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

export * from './types';
