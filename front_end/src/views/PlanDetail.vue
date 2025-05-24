<template>
  <div class="h-full overflow-y-auto">
    <div class="flex items-center border-b border-white/20 bg-slate-900/80 p-2">
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

    <AsyncContainer :loadingComponent="PlanDetailSkeleton" :errorComponent="PlanDetailError">
      <PlanDetail :planId="planId" />
    </AsyncContainer>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ChevronLeft } from 'lucide-vue-next';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import { Button } from '@/components/ui/button';
import {
  PlanDetailError,
  PlanDetailSkeleton,
  PlanDetail,
} from '@/components/views/plan/PlanDetail';
import { ROUTES } from '@/router/routes';

// Vue Router
const router = useRouter();
const route = useRoute();

// planId 계산 속성
const planId = computed(() => Number(route.params.planId));

// 뒤로 가기
const goBack = () => {
  router.push({ name: ROUTES.PLANS.name });
};
</script>
