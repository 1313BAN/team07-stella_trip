<template>
  <section class="relative">
    <AsyncContainer :loadingComponent="FilteredPlansSkeleton" :errorComponent="FilteredPlansError">
      <MyPlans
        :parentScrollContainer="scrollContainerRef"
        :apiParams="{ page: 1, size: 100 }"
        @addPlanClick="handleAddPlanCardClick"
        @cardClick="handlePlanCardClick"
        @likeClick="handlePlanLikeClick"
        @tagClick="handlePlanTagClick"
      />
    </AsyncContainer>

    <!-- 여행 계획 추가 모달 -->
    <PlanFormModal
      :isOpen="isAddDialogOpen"
      :isEditMode="false"
      :initialData="null"
      :isSubmitting="isSubmitting"
      @update:open="isAddDialogOpen = $event"
      @submit="handlePlanSubmit"
    />
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { toast } from 'vue-sonner';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import { FilteredPlansSkeleton, FilteredPlansError } from '@/components/views/plan/FilteredPlans';
import PlanFormModal from '@/components/views/plan/PlanFormModal.vue';
import {
  type Plan,
  type Tag,
  createPlan,
  type CreatePlanRequest,
} from '@/services/api/domains/plan';
import { ROUTES } from '@/router/routes';
import MyPlans from '@/components/views/myPlan/myPlans/myPlans.vue';

const router = useRouter();
const scrollContainerRef = ref<HTMLElement | null>(null);
const isAddDialogOpen = ref(false);
const isSubmitting = ref(false);

/**
 * 플랜 카드 클릭 핸들러 - 플랜 수정 페이지로 이동
 * @param plan - 클릭된 플랜 정보
 */
const handlePlanCardClick = (plan: Plan) => {
  router.push({
    name: ROUTES.PLAN_MODIFY.name,
    params: { planId: plan.planId.toString() },
  });
};

/**
 * 플랜 추가 카드 클릭 핸들러 - 모달 열기
 */
const handleAddPlanCardClick = () => {
  isAddDialogOpen.value = true;
};

/**
 * 플랜 좋아요 클릭 핸들러
 * @param plan - 좋아요한 플랜 정보
 */
const handlePlanLikeClick = (plan: Plan) => {
  console.log('여행 계획 좋아요 클릭:', plan.title);
  // TODO: 좋아요 API 호출
};

/**
 * 플랜 태그 클릭 핸들러
 * @param tag - 클릭된 태그 정보
 */
const handlePlanTagClick = (tag: Tag) => {
  console.log('여행 계획 태그 클릭:', tag.name);
  // TODO: 태그 필터링 기능
};

/**
 * 플랜 생성/수정 제출 핸들러
 * @param formData - 폼에서 제출된 데이터
 */
const handlePlanSubmit = async (formData: CreatePlanRequest) => {
  if (isSubmitting.value) return;

  try {
    isSubmitting.value = true;

    // 새 플랜 생성
    const plan = await createPlan(formData);

    toast.success('여행 계획이 성공적으로 추가되었습니다!', {
      description: `${formData.title} 계획이 생성되었습니다.`,
      duration: 4000,
    });

    // 생성된 플랜의 수정 페이지로 이동
    router.push({
      name: ROUTES.PLAN_MODIFY.name,
      params: { planId: plan.planId.toString() },
      state: { plan },
    });

    // 모달 닫기
    isAddDialogOpen.value = false;
  } catch (error) {
    console.error('계획 추가 실패:', error);

    toast.error('계획 추가에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isSubmitting.value = false;
  }
};
</script>
