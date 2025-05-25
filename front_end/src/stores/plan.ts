import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PlanDetail, RouteAttraction, RouteInfo } from '@/services/api/domains/plan';
import { fillPlanDetails } from '@/utils/planUtils';

export const usePlanStore = defineStore('plan', () => {
  const currentPlan = ref<PlanDetail | null>(null);
  const isModifying = ref<boolean>(false);
  const hasLock = ref<boolean>(false);

  // 변경된 날짜들을 추적
  const modifiedDates = ref<Set<string>>(new Set());

  // 삭제된 routeId들을 추적 (routeId -> { dayIndex, order })
  const deletedRoutes = ref<Map<number, { dayIndex: number; order: number }>>(new Map());

  /**
   * 플랜 데이터를 설정하고 모든 초기화 작업을 수행합니다.
   * @param plan - 설정할 플랜 데이터
   */
  const setPlanDetail = (plan: PlanDetail | null) => {
    if (!plan) {
      currentPlan.value = null;
      return;
    }

    // fillPlanDetails 유틸 함수를 사용하여 모든 날짜 채우기
    const filledData = fillPlanDetails(plan);

    // 초기화된 데이터 설정
    currentPlan.value = filledData;
  };

  /**
   * 수정 모드를 설정합니다.
   * @param modifying - 수정 모드 여부
   */
  const setModifying = (modifying: boolean) => {
    isModifying.value = modifying;
    if (!modifying) {
      // 수정 모드 종료 시 변경사항 초기화
      clearModifiedDates();
      clearDeletedRoutes();
    }
  };

  /**
   * 수정 모드를 토글합니다.
   */
  const toggleModifying = () => {
    isModifying.value = !isModifying.value;
    if (!isModifying.value) {
      clearModifiedDates();
      clearDeletedRoutes();
    }
  };

  /**
   * 락 상태를 설정합니다.
   * @param locked - 락 보유 여부
   */
  const setLock = (locked: boolean) => {
    hasLock.value = locked;
  };

  /**
   * 변경된 날짜 목록을 초기화합니다.
   */
  const clearModifiedDates = () => {
    modifiedDates.value.clear();
  };

  /**
   * 삭제된 routes 목록을 초기화합니다.
   */
  const clearDeletedRoutes = () => {
    deletedRoutes.value.clear();
  };

  /**
   * 날짜를 수정된 것으로 표시합니다.
   * @param date - 수정된 날짜
   */
  const markDateAsModified = (date: string) => {
    modifiedDates.value.add(date);
  };

  /**
   * route를 삭제된 것으로 표시합니다.
   * @param routeId - 삭제된 route ID
   * @param date - 삭제된 날짜 (dayIndex 계산용)
   * @param order - 삭제 당시 order
   */
  const markRouteAsDeleted = (routeId: number, date: string, order: number) => {
    const dayIndex = calculateDayIndex(date);
    deletedRoutes.value.set(routeId, { dayIndex, order });
  };

  /**
   * 변경된 날짜들의 경로 정보를 RouteInfo 배열로 변환합니다.
   * @returns RouteInfo 배열
   */
  const getModifiedRoutes = (): RouteInfo[] => {
    if (!currentPlan.value?.details) return [];

    const routes: RouteInfo[] = [];

    // 1. 현재 존재하는 항목들 (수정/순서변경된 항목들)
    modifiedDates.value.forEach(date => {
      const attractions = currentPlan.value!.details![date] || [];
      const dayIndex = calculateDayIndex(date);

      attractions.forEach(attraction => {
        routes.push({
          routeId: attraction.routeId,
          dayIndex,
          order: attraction.order,
          deleted: false,
        });
      });
    });

    // 2. 삭제된 항목들
    deletedRoutes.value.forEach(({ dayIndex, order }, routeId) => {
      routes.push({
        routeId,
        dayIndex,
        order,
        deleted: true,
      });
    });

    return routes;
  };

  /**
   * 날짜를 dayIndex로 변환합니다.
   * @param date - 변환할 날짜 (YYYY-MM-DD)
   * @returns dayIndex (1부터 시작)
   */
  const calculateDayIndex = (date: string): number => {
    if (!currentPlan.value?.startDate) return 1;

    const targetDate = new Date(date);
    const startDate = new Date(currentPlan.value.startDate);
    const diffTime = targetDate.getTime() - startDate.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    return diffDays + 1;
  };

  /**
   * 특정 날짜에 여행지를 추가합니다.
   * @param date - 대상 날짜
   * @param attraction - 추가할 여행지 정보
   */
  const addAttractionToDate = (date: string, attraction: RouteAttraction) => {
    if (currentPlan.value?.details) {
      if (!currentPlan.value.details[date]) {
        currentPlan.value.details[date] = [];
      }

      // order 자동 설정 (해당 날짜의 마지막 order + 1)
      const existingAttractions = currentPlan.value.details[date];
      const maxOrder =
        existingAttractions.length > 0 ? Math.max(...existingAttractions.map(a => a.order)) : 0;

      const newAttraction = {
        ...attraction,
        order: maxOrder + 1,
      };

      currentPlan.value.details[date].push(newAttraction);
      markDateAsModified(date);
    }
  };

  /**
   * 특정 날짜의 여행지를 제거합니다.
   * @param date - 대상 날짜
   * @param routeId - 제거할 여행지의 ID
   */
  const removeAttractionFromDate = (date: string, routeId: number) => {
    if (currentPlan.value?.details?.[date]) {
      // 삭제할 항목 찾기
      const targetAttraction = currentPlan.value.details[date].find(
        attraction => attraction.routeId === routeId
      );

      if (targetAttraction) {
        // 삭제 추적에 추가
        markRouteAsDeleted(routeId, date, targetAttraction.order);
      }

      // 실제 데이터에서 제거
      const filteredAttractions = currentPlan.value.details[date].filter(
        attraction => attraction.routeId !== routeId
      );

      // 제거 후 order 재정렬 (1부터 시작)
      const reorderedAttractions = filteredAttractions.map((attraction, index) => ({
        ...attraction,
        order: index + 1,
      }));

      currentPlan.value.details[date] = reorderedAttractions;
      markDateAsModified(date);
    }
  };

  /**
   * 여행지 순서를 변경합니다.
   * @param date - 대상 날짜
   * @param attractions - 새로운 순서의 여행지 배열
   */
  const reorderAttractions = (date: string, attractions: RouteAttraction[]) => {
    if (currentPlan.value?.details) {
      // order 속성을 인덱스에 맞게 재정렬 (0번째 인덱스 = order: 1)
      const reorderedAttractions = attractions.map((attraction, index) => ({
        ...attraction,
        order: index + 1,
      }));

      currentPlan.value.details[date] = reorderedAttractions;
      markDateAsModified(date);
    }
  };

  /**
   * 특정 날짜의 여행지를 order 기준으로 정렬합니다.
   * @param date - 대상 날짜
   */
  const sortAttractionsByOrder = (date: string) => {
    if (currentPlan.value?.details?.[date]) {
      currentPlan.value.details[date].sort((a, b) => a.order - b.order);
    }
  };

  /**
   * 모든 날짜의 여행지 order를 재정렬합니다.
   */
  const normalizeAllOrders = () => {
    if (currentPlan.value?.details) {
      Object.keys(currentPlan.value.details).forEach(date => {
        const attractions = currentPlan.value!.details![date];
        const reorderedAttractions = attractions
          .sort((a, b) => a.order - b.order) // 기존 order로 정렬
          .map((attraction, index) => ({
            ...attraction,
            order: index + 1, // 새로운 order 할당
          }));

        currentPlan.value!.details![date] = reorderedAttractions;
      });
    }
  };

  /**
   * 플랜을 초기화합니다.
   */
  const clearPlan = () => {
    currentPlan.value = null;
    isModifying.value = false;
    hasLock.value = false;
    clearModifiedDates();
    clearDeletedRoutes();
  };

  /**
   * 수정사항이 있는지 확인합니다.
   * @returns 수정사항 존재 여부
   */
  const hasModifications = (): boolean => {
    return modifiedDates.value.size > 0 || deletedRoutes.value.size > 0;
  };

  return {
    // State
    currentPlan,
    isModifying,
    hasLock,
    modifiedDates,
    deletedRoutes,

    // Actions
    setPlanDetail,
    setModifying,
    toggleModifying,
    setLock,
    clearModifiedDates,
    clearDeletedRoutes,
    markDateAsModified,
    markRouteAsDeleted,
    getModifiedRoutes,
    addAttractionToDate,
    removeAttractionFromDate,
    reorderAttractions,
    sortAttractionsByOrder,
    normalizeAllOrders,
    clearPlan,
    hasModifications,
  };
});
