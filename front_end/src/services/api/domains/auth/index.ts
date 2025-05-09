import { AUTH } from '../../endpoint';
import api from '../../api';
import type { ApiResponse } from '../../types';
import type {
  LoginRequest,
  LoginResponse,
  SignupRequest,
  LogoutResponse,
  SignoutResponse,
  SignupResponse,
} from './types';

/**
 * 사용자 로그인
 * @param data 로그인 요청 데이터
 * @returns 로그인 응답 (사용자 정보 + 토큰)
 */
export const login = async (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
  const response = await api.post<ApiResponse<LoginResponse>>(AUTH.LOGIN, data);
  return response.data;
};

/**
 * 사용자 로그아웃
 * @returns 로그아웃 성공 여부
 */
export const logout = async (): Promise<ApiResponse<LogoutResponse>> => {
  const response = await api.post<ApiResponse<LogoutResponse>>(AUTH.LOGOUT);
  return response.data;
};

/**
 * 회원가입
 * @param data 등록 요청 데이터
 * @returns 등록 후 로그인 응답 (사용자 정보 + 토큰)
 */
export const signup = async (data: SignupRequest): Promise<ApiResponse<SignupResponse>> => {
  const response = await api.post<ApiResponse<SignupResponse>>(AUTH.SIGNUP, data);
  return response.data;
};

/**
 * 회원 탈퇴
 * @returns 회원 탈퇴 성공 여부
 */
export const signout = async (): Promise<ApiResponse<SignoutResponse>> => {
  const response = await api.post<ApiResponse<SignoutResponse>>(AUTH.SIGNOUT);
  return response.data;
};
