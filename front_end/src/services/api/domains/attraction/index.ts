import { ATTRACTION } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse } from '../../types';
import type { Attraction, AttractionsParams } from './types';

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
    }
  );

  return response.data.body;
};

export * from './types';
