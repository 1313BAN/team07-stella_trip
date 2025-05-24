import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PlanDetail, RouteAttraction } from '@/services/api/domains/plan';
import { fillPlanDetails } from '@/utils/planUtils';

export const usePlanStore = defineStore('plan', () => {
  const currentPlan = ref<PlanDetail | null>(null);
  const isModifying = ref<boolean>(false);

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
  };

  /**
   * 수정 모드를 토글합니다.
   */
  const toggleModifying = () => {
    isModifying.value = !isModifying.value;
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
    }
  };

  /**
   * 특정 날짜의 여행지를 제거합니다.
   * @param date - 대상 날짜
   * @param routeId - 제거할 여행지의 ID
   */
  const removeAttractionFromDate = (date: string, routeId: number) => {
    if (currentPlan.value?.details?.[date]) {
      const filteredAttractions = currentPlan.value.details[date].filter(
        attraction => attraction.routeId !== routeId
      );

      // 제거 후 order 재정렬 (1부터 시작)
      const reorderedAttractions = filteredAttractions.map((attraction, index) => ({
        ...attraction,
        order: index + 1,
      }));

      currentPlan.value.details[date] = reorderedAttractions;
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
  };

  return {
    // State
    currentPlan,
    isModifying,

    // Actions
    setPlanDetail,
    setModifying,
    toggleModifying,
    addAttractionToDate,
    removeAttractionFromDate,
    reorderAttractions,
    sortAttractionsByOrder,
    normalizeAllOrders,
    clearPlan,
  };
});
