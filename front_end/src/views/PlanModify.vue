<template>
  <section class="relative h-[calc(100vh-4rem)]">
    <ResizablePanelGroup direction="horizontal" class="h-full">
      <ResizablePanel :defaultSize="33" :minSize="20" :maxSize="60" class="relative">
        <router-view v-slot="{ Component }">
          <template v-if="Component">
            <!-- 여행지 검색 화면 -->
            <component :is="Component" :currentDate="selectedDate" />
          </template>
          <template v-else>
            <AsyncContainer
              :loadingComponent="PlanDetailSkeleton"
              :errorComponent="PlanDetailError"
            >
              <PlanDetail
                :planId="planId"
                class="h-full overflow-x-hidden overflow-y-auto"
                @addAttraction="handleMoveToAttractionSearch"
              />
            </AsyncContainer>
          </template>
        </router-view>
      </ResizablePanel>

      <ResizableHandle withHandle />

      <ResizablePanel :defaultSize="67" class="relative" @resizeend="handleMapResize">
        <AsyncContainer :loadingComponent="CommonSkeleton" :errorComponent="MapError">
          <MapContainer
            ref="mapRef"
            :center="mapCenter"
            :level="mapLevel"
            :showCenterMarker="false"
          />
        </AsyncContainer>

        <!-- 사이드바 - 지도 영역 위에 오버레이 -->
        <div
          v-if="selectedAttraction"
          class="absolute top-0 left-0 z-20 m-1 h-full w-1/2 max-w-md rounded-md shadow-lg backdrop-blur-sm transition-all duration-300"
        >
          <AsyncContainer
            :loadingComponent="AttractionDetailSkeleton"
            :errorComponent="AttractionDetailError"
            :attractionName="selectedAttraction.name"
          >
            <AttractionDetail
              :attractionId="selectedAttraction.attractionId"
              :attractionName="selectedAttraction.name"
              :attraction="selectedAttraction"
              @close="closeAttractionDetail"
              @addToPlan="handleAddAttraction"
            />
          </AsyncContainer>
        </div>
      </ResizablePanel>
    </ResizablePanelGroup>
  </section>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import MapContainer from '@/components/map/MapContainer.vue';
import MapError from '@/components/map/MapError.vue';
import {
  PlanDetailError,
  PlanDetailSkeleton,
  PlanDetail,
} from '@/components/views/plan/PlanDetail';
import {
  AttractionDetail,
  AttractionDetailError,
  AttractionDetailSkeleton,
} from '@/components/views/attraction/AttractionDetail';
import CommonSkeleton from '@/components/skeleton/CommonSkeleton/CommonSkeleton.vue';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { usePlanStore } from '@/stores/plan';
import { addAttractionToDate, type PlanAttractionRequest } from '@/services/api/domains/plan';
import { useMapState } from '@/composables/useMapState';
import { ROUTES } from '@/router/routes';

const route = useRoute();
const router = useRouter();
const planStore = usePlanStore();
const { mapRef, mapLevel, mapCenter, selectedAttraction } = useMapState();

const planId = computed(() => Number(route.params.planId));
const selectedDate = ref<string | null>(null);

const handleMapResize = () => {
  if (mapRef.value) {
    mapRef.value.refreshMap();
  }
};

const handleMoveToAttractionSearch = (date: string) => {
  selectedDate.value = date;

  router.push({
    name: ROUTES.PLAN_MODIFY_ATTRACTION.name,
    params: { planId: planId.value },
    query: { date },
  });
};

// 여행지 추가 핸들러
const handleAddAttraction = async (attractionId: number) => {
  if (!selectedDate.value || !planStore.currentPlan?.startDate) {
    throw new Error('필수 날짜 정보가 없습니다.');
    return;
  }
  const dayIndex = calculateDayIndex(selectedDate.value, planStore.currentPlan?.startDate);

  const requestData: PlanAttractionRequest = {
    dayIndex,
    attractionId,
    visitTime: '',
    memo: '',
  };

  // API 호출하여 계획 업데이트
  const updatedPlan = await addAttractionToDate(planId.value, requestData);

  // store 전체 업데이트
  planStore.setPlanDetail(updatedPlan);

  selectedAttraction.value = null;
  router.back();
};

// dayIndex 계산 함수
function calculateDayIndex(selectedDate: string, planStartDate: string): number {
  if (!selectedDate || !planStartDate) {
    throw new Error('날짜 값이 유효하지 않습니다');
  }

  const selected = new Date(selectedDate);
  const startDate = new Date(planStartDate);

  if (isNaN(selected.getTime()) || isNaN(startDate.getTime())) {
    throw new Error('날짜 형식이 올바르지 않습니다');
  }

  const timeDiff = selected.getTime() - startDate.getTime();
  const dayDiff = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

  return dayDiff + 1;
}

// 여행지 상세 사이드바 닫기
const closeAttractionDetail = () => {
  selectedAttraction.value = null;
};
</script>
