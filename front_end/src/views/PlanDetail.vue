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
      <PlanDetail :planId="planId" @toggleLike="handleToggleLike" />
    </AsyncContainer>
  </div>
</template>

<script setup lang="ts">
import { computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ChevronLeft } from 'lucide-vue-next';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import { Button } from '@/components/ui/button';
import {
  PlanDetailError,
  PlanDetailSkeleton,
  PlanDetail,
} from '@/components/views/plan/PlanDetail';
import { useMapState } from '@/composables/useMapState';
import { ROUTES } from '@/router/routes';

// Vue Router
const router = useRouter();
const route = useRoute();

// planId 계산 속성
const planId = computed(() => Number(route.params.planId));

// 지도 상태 관리
const { mapRef, showAllMarkers } = useMapState();

// MapContainer 컴포넌트의 map 객체 감시
// 여기가 핵심: mapRef.value.map이 변경될 때마다 마커를 다시 그림
watch(
  () => mapRef.value?.map,
  newMap => {
    if (newMap) {
      showAllMarkers();
    }
  },
  { deep: true }
);

// 좋아요 토글 핸들러
const handleToggleLike = (planId: number) => {
  console.log('여행 계획 좋아요 토글:', planId);
};

// 뒤로 가기
const goBack = () => {
  router.push({ name: ROUTES.PLANS.name });
};
</script>
