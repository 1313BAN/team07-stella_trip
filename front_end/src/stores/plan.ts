import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PlanDetail, RouteAttraction } from '@/services/api/domains/plan';

export const usePlanStore = defineStore('plan', () => {
  const currentPlan = ref<PlanDetail | null>(null);

  // 플랜 데이터 설정
  const setPlanDetail = (plan: PlanDetail | null) => {
    currentPlan.value = plan;
  };

  // 특정 날짜에 여행지 추가
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

  // 특정 날짜의 여행지 제거
  const removeAttractionFromDate = (date: string, routeId: number) => {
    if (currentPlan.value?.details?.[date]) {
      currentPlan.value.details[date] = currentPlan.value.details[date].filter(
        attraction => attraction.routeId !== routeId
      );
    }
  };

  // 여행지 순서 변경
  const reorderAttractions = (date: string, attractions: RouteAttraction[]) => {
    if (currentPlan.value?.details) {
      currentPlan.value.details[date] = attractions;
    }
  };

  // 플랜 초기화
  const clearPlan = () => {
    currentPlan.value = null;
  };

  return {
    currentPlan,
    setPlanDetail,
    addAttractionToDate,
    removeAttractionFromDate,
    reorderAttractions,
    clearPlan,
  };
});
