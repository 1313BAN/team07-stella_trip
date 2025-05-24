<template>
  <div class="flex flex-col divide-y divide-white/10">
    <!-- 별자리 표시 섹션 -->
    <StellaHeader :stella="planDetail?.stella" :backgroundStars="backgroundStars" />

    <!-- 기본 정보 섹션 -->
    <PlanInfo class="px-2" :plan="planDetail" @toggleLike="toggleLike" />

    <!-- 일정 섹션 -->
    <div class="flex-1 bg-slate-900/20 p-3">
      <div class="mb-3 flex items-center justify-between">
        <h3 class="p-2 text-lg font-semibold text-purple-200">일정</h3>
        <div v-if="selectedDate" class="flex items-center">
          <Badge variant="outline" class="border-purple-400/30 bg-purple-900/30 text-purple-200">
            {{ selectedDate }} 경로 표시 중
          </Badge>
          <Button
            variant="ghost"
            size="sm"
            class="ml-2 text-purple-200 hover:bg-purple-900/30"
            @click="resetRoute"
          >
            <X class="h-4 w-4" />
          </Button>
        </div>
      </div>

      <!-- 일별 일정 목록 -->
      <div v-if="planDetail?.details" class="space-y-4">
        <DailySchedule
          v-for="(attractions, date) in planDetail.details"
          :key="date"
          :date="date"
          :attractions="attractions"
          @showRoute="showRouteOnMap"
          @addAttraction="handleAddAttraction"
        />
      </div>

      <!-- 일정이 없을 경우 -->
      <EmptySchedule v-else />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onUnmounted, watch } from 'vue';
import { X } from 'lucide-vue-next';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import StellaHeader from './StellaHeader.vue';
import PlanInfo from './PlanInfo.vue';
import DailySchedule from '@/components/card/DailyScheduleCard/DailyScheduleCard.vue';
import EmptySchedule from './EmptySchedule.vue';
import { getPlanDetail, type PlanDetail, type RouteAttraction } from '@/services/api/domains/plan';
import { useMapState } from '@/composables/useMapState';
import { fillPlanDetails } from '@/utils/planUtils';
import { usePlanStore } from '@/stores/plan';
import type { MarkerInfo } from '@/types/kakao';

const props = defineProps<{
  planId: number;
}>();

// Emits
const emit = defineEmits<{
  (e: 'moveToAttractionSearch', date: string): void;
}>();

const { selectPlanDetail, showRoute, clearPolylines, clearMarkers } = useMapState();
const planStore = usePlanStore();

// Store에서 현재 플랜 데이터 가져오기
const planDetail = computed(() => planStore.currentPlan);
const selectedDate = ref<string | null>(null);

// 여행지 추가 버튼 클릭 핸들러
const handleAddAttraction = (date: string) => {
  emit('moveToAttractionSearch', date);
};

// 좋아요 토글
const toggleLike = (planId: number) => {};

// 배경 별 생성
const backgroundStars = Array.from({ length: 30 }, () => ({
  x: Math.random() * 100,
  y: Math.random() * 100,
  r: Math.random() * 0.8 + 0.2,
  brightness: Math.random() * 0.4 + 0.2,
  duration: Math.random() * 2 + 2,
}));

// RouteAttraction을 MarkerInfo로 변환
const convertToMarkerInfos = (attractions: RouteAttraction[], date: string): MarkerInfo[] => {
  return attractions
    .map(attr => ({
      lat: parseFloat(String(attr.latitude)),
      lng: parseFloat(String(attr.longitude)),
      name: attr.name,
      order: attr.order,
      date: date,
    }))
    .filter(info => !isNaN(info.lat) && !isNaN(info.lng));
};

// 일별 경로 지도에 표시
const showRouteOnMap = (date: string, attractions: RouteAttraction[]) => {
  selectedDate.value = date;

  if (!planDetail.value) return;

  const dailyPlanDetail = {
    ...planDetail.value,
    details: {
      [date]: attractions,
    },
  } as PlanDetail;

  // 다중 마커 표시
  selectPlanDetail(dailyPlanDetail);

  // 폴리라인 표시
  const markerInfos = convertToMarkerInfos(attractions, date);
  if (markerInfos.length >= 2) {
    showRoute(markerInfos);
  }
};

// 경로 초기화
const resetRoute = () => {
  selectedDate.value = null;

  clearPolylines();

  if (planDetail.value) {
    selectPlanDetail(planDetail.value as PlanDetail);
  }
};

// 비동기 초기화 데이터 로드
const initializeData = async () => {
  const data = await getPlanDetail(props.planId);

  // 모든 날짜에 대해 details 채우기
  const filledData = data ? fillPlanDetails(data) : null;
  planStore.setPlanDetail(filledData);

  if (filledData) {
    selectPlanDetail(filledData);
  }

  return filledData;
};

onUnmounted(() => {
  clearPolylines();
  clearMarkers();
});

await initializeData();

watch(
  () => props.planId,
  async (newId, oldId) => {
    if (newId !== oldId) {
      await initializeData();
    }
  }
);
</script>
