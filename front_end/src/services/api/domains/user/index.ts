import { AUTH } from '../../endpoint';
import api from '../../api';
import type { ApiResponse, PagenationResponse } from '../../types';
import type { BasicUserInfo, UserInfo } from './types';
import type { Attraction } from '../attraction/types';
import type { Plan } from '../plan';

/**
 * 사용자 정보 타입 정의
 * @param userId 사용자 ID
 * @returns 사용자 정보
 */
export const getUserInfo = async (): Promise<UserInfo> => {
  const response = await api.get<ApiResponse<UserInfo>>(AUTH.PROFILE);
  return response.data.body;
};

export const putBasicUserInfo = async (userInfo: BasicUserInfo): Promise<UserInfo> => {
  const response = await api.put<ApiResponse<UserInfo>>(AUTH.BASIC_PROFILE, userInfo);
  return response.data.body;
};

export const putPasswordChange = async (newPassword: string): Promise<void> => {
  const response = await api.put<ApiResponse<void>>(AUTH.PASSWORD_CHANGE, {
    password: newPassword,
  });
  return response.data.body;
};

export const getLikedAttractions = async (): Promise<PagenationResponse<Attraction>> => {
  const response = await api.get<ApiResponse<PagenationResponse<Attraction>>>(
    AUTH.LIKED_ATTRACTIONS
  );
  return response.data.body;
};

export const getLikedPlans = async (): Promise<PagenationResponse<Plan>> => {
  const response = await api.get<ApiResponse<PagenationResponse<Plan>>>(AUTH.LIKED_PLANS);
  return response.data.body;
};
