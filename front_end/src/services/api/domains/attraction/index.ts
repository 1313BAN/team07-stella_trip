import { ATTRACTION } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse } from '../../types';
import type { Attraction, AttractionsParams, AttractionDetail, Sigungu } from './types';

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
