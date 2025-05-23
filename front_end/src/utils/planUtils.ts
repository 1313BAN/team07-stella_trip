import type { PlanDetail, RouteAttraction } from '@/services/api/domains/plan';

/**
 * 시작일과 종료일 사이의 모든 날짜를 생성합니다.
 */
export const generateDateRange = (startDate: string, endDate: string): string[] => {
  const dates: string[] = [];
  const start = new Date(startDate);
  const end = new Date(endDate);

  const currentDate = new Date(start);
  while (currentDate <= end) {
    dates.push(currentDate.toISOString().split('T')[0]);
    currentDate.setDate(currentDate.getDate() + 1);
  }

  return dates;
};

/**
 * PlanDetail의 details를 시작일~종료일 사이의 모든 날짜로 채움
 * 기존 데이터가 없는 날짜는 빈 배열로 초기화
 */
export const fillPlanDetails = (planDetail: PlanDetail): PlanDetail => {
  if (!planDetail.startDate || !planDetail.endDate) {
    return planDetail;
  }

  const dateRange = generateDateRange(planDetail.startDate, planDetail.endDate);
  const filledDetails: Record<string, RouteAttraction[]> = {};

  // 모든 날짜를 기본값으로 초기화
  dateRange.forEach(date => {
    filledDetails[date] = [];
  });

  // 기존 데이터가 있으면 덮어쓰기
  if (planDetail.details) {
    Object.entries(planDetail.details).forEach(([date, attractions]) => {
      if (Object.prototype.hasOwnProperty.call(filledDetails, date)) {
        filledDetails[date] = attractions;
      }
    });
  }

  return {
    ...planDetail,
    details: filledDetails,
  };
};
