<template>
  <div class="flex flex-col divide-y divide-white/10">
    <!-- 별자리 표시 섹션 -->
    <StellaHeader :stella="planDetail!.stella" :backgroundStars="backgroundStars" />

    <!-- 기본 정보 섹션 -->
    <PlanInfo
      :title="planDetail?.title"
      :dateRange="formatDateRange(planDetail?.startDate, planDetail?.endDate)"
      :description="planDetail?.description ?? ''"
      :tags="planDetail?.tags"
      :writers="planDetail?.planWriters"
      :likeCount="planDetail?.likeCount || 0"
      :isLiked="planDetail?.isLiked"
      @toggleLike="toggleLike"
    />

    <!-- 일정 섹션 -->
    <div class="flex-1 bg-slate-900/20 p-3">
      <div class="mb-3 flex items-center justify-between">
        <h3 class="font-semibold text-purple-200">일정</h3>
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
        />
      </div>

      <!-- 일정이 없을 경우 -->
      <EmptySchedule v-else />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onUnmounted, watch } from 'vue';
import { X } from 'lucide-vue-next';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import StellaHeader from './StellaHeader.vue';
import PlanInfo from './PlanInfo.vue';
import DailySchedule from './DailySchedule.vue';
import EmptySchedule from './EmptySchedule.vue';
import { getPlanDetail, type PlanDetail, type RouteAttraction } from '@/services/api/domains/plan';
import { useMapState } from '@/composables/useMapState';
import type { MarkerInfo } from '@/types/kakao';

// Props
const props = defineProps<{
  planId: number;
}>();

// Emits
const emit = defineEmits<{
  (e: 'toggleLike', planId: number): void;
}>();

const { selectPlanDetail, showRoute, clearPolylines, clearMarkers } = useMapState();

const planDetail = ref<PlanDetail | null>(null);
const selectedDate = ref<string | null>(null);

// 날짜 범위 표시 형식
const formatDateRange = (startDate?: string, endDate?: string) => {
  if (!startDate || !endDate) return '';

  const start = new Date(startDate);
  const end = new Date(endDate);

  const startStr = `${start.getFullYear()}년 ${start.getMonth() + 1}월 ${start.getDate()}일`;
  const endStr = `${end.getFullYear()}년 ${end.getMonth() + 1}월 ${end.getDate()}일`;

  return `${startStr} ~ ${endStr}`;
};

// 좋아요 토글
const toggleLike = () => {
  if (planDetail.value) {
    emit('toggleLike', planDetail.value.planId);
  }
};

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

  const dailyPlanDetail: PlanDetail = {
    ...planDetail.value,
    details: {
      [date]: attractions,
    },
  };

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
    selectPlanDetail(planDetail.value);
  }
};

// 비동기 초기화 데이터 로드
const initializeData = async () => {
  const data = await getPlanDetail(props.planId);
  planDetail.value = data;

  if (data) {
    selectPlanDetail(data);
  }

  return data;
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
