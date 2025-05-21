<template>
  <div class="flex flex-col divide-y divide-white/10">
    <!-- 별자리 표시 섹션 -->
    <StellaHeader :stella="planDetail?.stella" :backgroundStars="generateBackgroundStars()" />

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
import { ref, onUnmounted } from 'vue';
import { X } from 'lucide-vue-next';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import StellaHeader from './StellaHeader.vue';
import PlanInfo from './PlanInfo.vue';
import DailySchedule from './DailySchedule.vue';
import EmptySchedule from './EmptySchedule.vue';
import { getPlanDetail, type PlanDetail, type RouteAttraction } from '@/services/api/domains/plan';
import { useMapState } from '@/composables/useMapState';

// Props
const props = defineProps<{
  planId: string;
}>();

// Emits
const emit = defineEmits<{
  (e: 'toggleLike', planId: number): void;
}>();

// 상태 관리 - 단일 참조 방식으로 변경
const { selectPlanDetail, showRoute, clearPolylines } = useMapState();
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
const generateBackgroundStars = () => {
  return Array.from({ length: 30 }, () => ({
    x: Math.random() * 100,
    y: Math.random() * 100,
    r: Math.random() * 0.8 + 0.2,
    brightness: Math.random() * 0.4 + 0.2,
    duration: Math.random() * 2 + 2,
  }));
};

// 일별 경로 지도에 표시
const showRouteOnMap = (date: string, attractions: RouteAttraction[]) => {
  selectedDate.value = date;

  // 경로 정보를 생성하여 지도에 표시
  if (planDetail.value) {
    // 해당 날짜의 경로만 포함하는 임시 PlanDetail 객체 생성
    const dailyPlanDetail = {
      ...planDetail.value,
      details: {
        [date]: attractions,
      },
    };

    // 마커 표시
    selectPlanDetail(dailyPlanDetail);

    // 경로선 표시
    // MarkerInfo 형식으로 변환
    const markerInfos = attractions.map(attr => ({
      lat: parseFloat(String(attr.latitude)),
      lng: parseFloat(String(attr.longitude)),
      name: attr.name,
      order: attr.order,
      date: date,
    }));

    // 경로 그리기
    showRoute(markerInfos);
  }
};

// 경로 초기화
const resetRoute = () => {
  selectedDate.value = null;

  // 기존 경로선 제거
  clearPolylines();

  // 전체 경로를 다시 표시
  if (planDetail.value) {
    selectPlanDetail(planDetail.value);
  }
};

// 비동기 초기화 데이터 로드
const initializeData = async () => {
  // planId가 유효한지 확인
  const planId = Number(props.planId);

  if (isNaN(planId)) {
    throw new Error('유효하지 않은 계획 ID');
  }

  // 계획 상세 정보 로드
  const data = await getPlanDetail(planId);
  planDetail.value = data;

  // 이제 selectPlanDetail 호출 - 맵 관련 작업 수행
  if (data) {
    selectPlanDetail(data);
  }

  return data;
};

onUnmounted(() => {
  clearPolylines();
});

// AsyncContainer와 Suspense와 함께 사용하기 위해 setup에서 직접 호출
await initializeData();
</script>
