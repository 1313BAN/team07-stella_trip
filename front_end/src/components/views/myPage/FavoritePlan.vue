<script setup lang="ts">
import PlanCard from '@/components/card/PlanCard/PlanCard.vue';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import { computed, onMounted, ref } from 'vue';
import type { Plan } from '@/services/api/domains/plan/types';
import { getLikedPlans } from '@/services/api/domains/user';
import router from '@/router';
import { ROUTES } from '@/router/routes';
import type { Tag } from '@/services/api/domains/plan/types';
import { useAuthStore } from '@/stores/auth';
import { toast } from 'vue-sonner';
import { deletePlanLike, postPlanLike } from '@/services/api/domains/plan';

const plans = ref<Plan[]>([]);

const fetchPlans = async () => {
  plans.value = (await getLikedPlans()).content;
};

onMounted(async () => {
  // 초기화 로직: 예시로 빈 배열을 할당
  await fetchPlans();
});

// 활성 탭 상태

const handlePlanCardClick = (plan: Plan) => {
  router.push({
    name: ROUTES.PLAN_DETAIL.name,
    params: { planId: plan.planId.toString() },
  });
};

const isLoggedIn = computed(() => {
  const authStore = useAuthStore();
  return authStore.isAuthenticated;
});

const handlePlanLikeClick = (plan: Plan) => {
  if (!isLoggedIn.value) {
    toast('로그인이 필요한 기능입니다.');
    return;
  }
  if (!plan.isLiked) {
    postPlanLike(plan.planId)
      .then(() => {
        plan.isLiked = true;
        plan.likeCount = (plan.likeCount || 0) + 1; // 좋아요 수 증가
        toast('여행 계획 좋아요 추가 성공', {
          description: `여행 계획 "${plan.title}"에 좋아요가 추가되었습니다.`,
        });
      })
      .catch(() => {
        toast.error('여행 계획 좋아요 추가 실패', {
          description: `여행 계획 "${plan.title}"에 좋아요 추가에 실패했습니다.`,
        });
        plan.isLiked = false; // 실패 시 원래 상태로 되돌리기
      });
  } else {
    deletePlanLike(plan.planId)
      .then(() => {
        plan.isLiked = false;
        plan.likeCount = (plan.likeCount || 0) - 1; // 좋아요 수 감소
        toast('여행 계획 좋아요 제거 성공', {
          description: `여행 계획 "${plan.title}"의 좋아요가 제거되었습니다.`,
        });
      })
      .catch(() => {
        toast.error('여행 계획 좋아요 제거 실패', {
          description: `여행 계획 "${plan.title}"의 좋아요 제거에 실패했습니다.`,
        });
        plan.isLiked = true; // 실패 시 원래 상태로 되돌리기
      });
  }
};
const handlePlanTagClick = (tag: Tag) => {
  router.push({
    path: ROUTES.PLANS.path,
    query: {
      search: tag.name,
    },
  });
};
</script>

<template>
  <div class="mb-4">
    <template v-if="plans.length === 0">
      <div class="h-full w-full pt-16 text-center">
        <p class="text-lg text-gray-200">검색 결과가 없습니다.</p>
      </div>
    </template>
    <template v-else>
      <GridCardListContainer :showTitle="false">
        <PlanCard
          v-for="plan in plans"
          :key="plan.planId"
          :plan="plan"
          @cardClick="handlePlanCardClick(plan)"
          @likeClick="handlePlanLikeClick(plan)"
          @tagClick="handlePlanTagClick"
        />
      </GridCardListContainer>
    </template>
  </div>
</template>
