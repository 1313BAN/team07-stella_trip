import { PLAN } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse } from '../../types';
import type { Plan, PlansParams, Tag } from './types';

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
export const getPopularTags = async (): Promise<Tag[]> => {
  const response = await api.get<ApiResponse<Tag[]>>(PLAN.TAGS);

  return response.data.body;
};

export * from './types';
