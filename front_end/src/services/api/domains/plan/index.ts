import { PLAN } from '../../endpoint';
import api from '../../api';
import type {
  ApiResponse,
  PagenationResponse,
  PaginationParams,
  CommonSuccessBody,
} from '../../types';
import type {
  Plan,
  PlansParams,
  Tag,
  PlanDetail,
  CreatePlanRequest,
  PlanAttractionRequest,
  UpdatePlanInfoRequest,
  UpdatePlanScheduleRequest,
  LockResponse,
  UpdateRouteRequest,
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
export const createPlan = async (data: CreatePlanRequest): Promise<PlanDetail> => {
  const response = await api.post<ApiResponse<PlanDetail>>(PLAN.PLANS, data);

  return response.data.body;
};

/**
 * 여행 계획 수정 (락 확인 필요 X)
 * @param data 여행 계획 데이터
 * @returns 변경된 여행 계획 정보
 */
export const updatePlanInfo = async (
  planId: number,
  data: UpdatePlanInfoRequest
): Promise<PlanDetail> => {
  const response = await api.put<ApiResponse<PlanDetail>>(PLAN.DETAIL(planId), data);

  return response.data.body;
};

/**
 * 여행 계획 일정 수정 (락 확인 필요)
 * @param data 여행 계획 일정 데이터
 * @returns 변경된 여행 계획 정보
 */
export const updatePlanSchedule = async (
  planId: number,
  data: UpdatePlanScheduleRequest
): Promise<PlanDetail> => {
  const response = await api.put<ApiResponse<PlanDetail>>(PLAN.SCHEDULE(planId), data);

  return response.data.body;
};

/**
 * 여행 계획 경로 수정 (락 확인 필요)
 * @param planId planId
 * @param data 경로 데이터
 * @returns 변경된 여행 계획 정보
 */
export const updateRoute = async (
  planId: number,
  data: UpdateRouteRequest
): Promise<PlanDetail> => {
  const response = await api.patch<ApiResponse<PlanDetail>>(PLAN.ATTRACTIONS(planId), data);

  return response.data.body;
};

/**
 * 여행 계획의 특정 일자에 여행지 추가
 * @param planId planId
 * @param data 특정 일자 index (1부터 시작)
 * @returns 업데이트된 여행 계획
 */
export const addAttractionToDate = async (
  planId: number,
  data: PlanAttractionRequest
): Promise<PlanDetail> => {
  const response = await api.post<ApiResponse<PlanDetail>>(PLAN.ATTRACTIONS(planId), data);

  return response.data.body;
};

// --- 락 --- //

/**
 * 락 확인
 * @param planId planId
 * @returns LockResponse
 */
export const checkLock = async (planId: number): Promise<LockResponse> => {
  const response = await api.get<ApiResponse<LockResponse>>(PLAN.LOCK_CHECK(planId));

  return response.data.body;
};

/**
 * 락 획득
 * @param planId planId
 * @returns LockResponse
 */
export const getLock = async (planId: number): Promise<CommonSuccessBody> => {
  const response = await api.post<ApiResponse<CommonSuccessBody>>(PLAN.LOCK(planId));

  return response.data.body;
};

/**
 * 락 반환
 * @param planId planId
 * @returns LockResponse
 */
export const returnLock = async (planId: number): Promise<CommonSuccessBody> => {
  const response = await api.post<ApiResponse<CommonSuccessBody>>(PLAN.UNLOCK(planId));

  return response.data.body;
};

// --- 사용자 여행 계획 --- //

/**
 * query parameter를 바탕으로 여행 계획 리스트 조회
 * @returns 여행 계획 리스트
 */
export const getMyPlans = async (params?: PaginationParams): Promise<PagenationResponse<Plan>> => {
  const response = await api.get<ApiResponse<PagenationResponse<Plan>>>(PLAN.MY_PLANS, {
    params,
  });

  return response.data.body;
};

/**
 * 여행에 이메일로 사용자 초대하기
 * @param planId planId
 * @returns
 */
export const inviteToMyPlan = async (planId: number, email: string): Promise<boolean> => {
  const response = await api.post<ApiResponse<boolean>>(PLAN.INVITE(planId), { email });
  return response.data.body;
};

/**
 * 여행에서 나가기
 * @param planId planId
 * @returns
 */
export const leaveMyPlan = async (planId: number): Promise<boolean> => {
  const response = await api.post<ApiResponse<boolean>>(PLAN.LEAVE(planId));

  return response.data.body;
};

export * from './types';
