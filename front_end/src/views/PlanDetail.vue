<template>
  <div class="h-full overflow-y-auto">
    <div class="flex items-center justify-between border-b border-white/20 bg-slate-900/80 p-2">
      <div class="flex items-center">
        <Button
          variant="ghost"
          size="icon"
          class="mr-2 h-8 w-8 text-gray-300 hover:bg-white/10 hover:text-white"
          @click="goBack"
        >
          <ChevronLeft class="h-5 w-5" />
        </Button>
        <h3 class="text-lg font-semibold text-white">여행 계획 상세</h3>
      </div>

      <Button
        variant="outline"
        size="sm"
        class="border-purple-500/50 bg-purple-900/30 text-purple-300 hover:bg-purple-800/50 hover:text-purple-200"
        @click="handleTogglePlanInfoEdit"
      >
        계획 정보 수정
      </Button>
    </div>

    <AsyncContainer :loadingComponent="PlanDetailSkeleton" :errorComponent="PlanDetailError">
      <PlanDetail
        :planId="planId"
        @moveToAttractionSearch="handleMoveToAttractionSearch"
        @toggleScheduleEdit="handleToggleScheduleEdit"
      />
    </AsyncContainer>

    <!-- 여행 계획 정보 수정 모달 -->
    <PlanFormModal
      :isOpen="isPlanInfoEditOpen"
      :isEditMode="true"
      :initialData="planStore.currentPlan"
      :isSubmitting="isSubmittingEdit"
      @update:open="isPlanInfoEditOpen = $event"
      @submit="handlePlanInfoSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { toast } from 'vue-sonner';
import { ChevronLeft } from 'lucide-vue-next';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import { Button } from '@/components/ui/button';
import {
  PlanDetailError,
  PlanDetailSkeleton,
  PlanDetail,
} from '@/components/views/plan/PlanDetail';
import PlanFormModal from '@/components/views/plan/PlanFormModal.vue';
import { usePlanStore } from '@/stores/plan';
import {
  updatePlanInfo,
  updatePlanSchedule,
  checkLock,
  type CreatePlanRequest,
  type UpdatePlanInfoRequest,
  type UpdatePlanScheduleRequest,
} from '@/services/api/domains/plan';

// Vue Router
const router = useRouter();
const route = useRoute();
const planStore = usePlanStore();

// planId 계산 속성
const planId = computed(() => Number(route.params.planId));

// 상태 관리
const isPlanInfoEditOpen = ref(false);
const isSubmittingEdit = ref(false);

const emit = defineEmits<{
  (e: 'moveToAttractionSearch', date: string): void;
}>();

/**
 * 여행지 추가 버튼 클릭 핸들러
 * @param date - 선택된 날짜
 */
const handleMoveToAttractionSearch = (date: string) => {
  emit('moveToAttractionSearch', date);
};

/**
 * 일정 편집 모드 토글
 */
const handleToggleScheduleEdit = () => {
  planStore.toggleModifying();
};

/**
 * 여행계획 정보 수정 모달 열기
 */
const handleTogglePlanInfoEdit = () => {
  isPlanInfoEditOpen.value = true;
};

/**
 * 여행계획 정보 수정 제출 핸들러
 * @param formData - 수정된 폼 데이터
 */
const handlePlanInfoSubmit = async (formData: CreatePlanRequest) => {
  if (isSubmittingEdit.value || !planStore.currentPlan) return;

  try {
    isSubmittingEdit.value = true;

    const originalPlan = planStore.currentPlan;

    // 일정 정보가 변경되었는지 확인
    const isScheduleChanged =
      formData.startDate !== originalPlan.startDate || formData.endDate !== originalPlan.endDate;

    if (isScheduleChanged) {
      // 일정 변경 시 락 확인
      const lockResponse = await checkLock(originalPlan.planId);

      if (lockResponse.lockStatus && lockResponse.userId !== originalPlan.planWriters[0]?.userId) {
        toast.error('다른 사용자가 수정 중입니다', {
          description: '잠시 후 다시 시도해주세요.',
          duration: 4000,
        });
        return;
      }

      // 일정 정보 업데이트 (락 확인 필요)
      const scheduleData: UpdatePlanScheduleRequest = {
        startDate: formData.startDate,
        endDate: formData.endDate,
      };

      await updatePlanSchedule(originalPlan.planId, scheduleData);
    }

    // 기본 정보 업데이트 (락 확인 불필요)
    const infoData: UpdatePlanInfoRequest = {
      title: formData.title,
      description: formData.description,
      tags: formData.tags,
      isPublic: formData.isPublic,
    };

    const updatedPlanResponse = await updatePlanInfo(originalPlan.planId, infoData);

    planStore.setPlanDetail(updatedPlanResponse);

    toast.success('여행 계획이 성공적으로 수정되었습니다!', {
      description: `${formData.title} 계획이 업데이트되었습니다.`,
      duration: 4000,
    });

    isPlanInfoEditOpen.value = false;
  } catch (error) {
    console.error('계획 수정 실패:', error);

    toast.error('계획 수정에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isSubmittingEdit.value = false;
  }
};

/**
 * 뒤로 가기
 */
const goBack = () => {
  router.back();
};
</script>
