import api from '../../api';
import { STELLA } from '../../endpoint';
import type { ApiResponse } from '../../types';
import type { StellaParams, StellaResponse } from './types';

/**
 * 별자리 정보를 바탕으로 공유 링크 생성
 * @param data planId, stellaData
 * @returns StellaResponse
 */
export const getShareLink = async (data: StellaParams): Promise<StellaResponse> => {
  const response = await api.post<ApiResponse<StellaResponse>>(STELLA.SHARE, data);

  return response.data.body;
};

/**
 * 공유 링크를 통해 별자리 정보, 여행 계획 정보 조회
 * @param params
 * @returns StellaResponse
 */
export const getSharedData = async (link: string): Promise<StellaResponse> => {
  const response = await api.get<ApiResponse<StellaResponse>>(STELLA.LINK(link));

  return response.data.body;
};
