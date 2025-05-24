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
        <div class="flex items-center gap-2">
          <!-- 일정 편집 버튼 -->
          <Button
            variant="outline"
            size="sm"
            class="border-blue-500/50 bg-blue-900/30 text-blue-300 hover:bg-blue-800/50 hover:text-blue-200"
            @click="handleToggleScheduleEdit"
          >
            {{ planStore.isModifying ? '편집 완료' : '일정 편집' }}
          </Button>

          <!-- 경로 표시 중 배지 -->
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
          @deleteAttraction="handleDeleteAttraction"
        />
      </div>

      <!-- 일정이 없을 경우 -->
      <EmptySchedule v-else />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import { X } from 'lucide-vue-next';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import StellaHeader from './StellaHeader.vue';
import PlanInfo from './PlanInfo.vue';
import DailySchedule from '@/components/card/DailyScheduleCard/DailyScheduleCard.vue';
import EmptySchedule from './EmptySchedule.vue';
import { getPlanDetail, type PlanDetail, type RouteAttraction } from '@/services/api/domains/plan';
import { useMapState } from '@/composables/useMapState';
import { usePlanStore } from '@/stores/plan';
import type { MarkerInfo } from '@/types/kakao';

const props = defineProps<{
  planId: number;
}>();

// Emits
const emit = defineEmits<{
  (e: 'moveToAttractionSearch', date: string): void;
  (e: 'toggleScheduleEdit'): void;
}>();

const { selectPlanDetail, showRoute, clearPolylines, clearMarkers } = useMapState();
const planStore = usePlanStore();

// Store에서 현재 플랜 데이터 가져오기 (reactive)
const planDetail = computed(() => planStore.currentPlan);
const selectedDate = ref<string | null>(null);

/**
 * 여행지 추가 버튼 클릭 핸들러
 * @param date - 선택된 날짜
 */
const handleAddAttraction = (date: string) => {
  emit('moveToAttractionSearch', date);
};

/**
 * 여행지 삭제 핸들러
 * @param date - 대상 날짜
 * @param routeId - 삭제할 여행지 ID
 */
const handleDeleteAttraction = (date: string, routeId: number) => {
  planStore.removeAttractionFromDate(date, routeId);

  // 현재 선택된 날짜의 경로가 표시 중이라면 업데이트
  if (selectedDate.value === date && planDetail.value?.details?.[date]) {
    const updatedAttractions = planDetail.value.details[date];
    if (updatedAttractions.length > 0) {
      showRouteOnMap(date, updatedAttractions);
    } else {
      resetRoute();
    }
  }
};

/**
 * 일정 편집 모드 토글
 */
const handleToggleScheduleEdit = () => {
  emit('toggleScheduleEdit');
};

/**
 * 좋아요 토글
 * @param planId - 플랜 ID
 */
const toggleLike = (planId: number) => {
  // TODO: 좋아요 기능 구현
};

// 배경 별 생성
const backgroundStars = Array.from({ length: 30 }, () => ({
  x: Math.random() * 100,
  y: Math.random() * 100,
  r: Math.random() * 0.8 + 0.2,
  brightness: Math.random() * 0.4 + 0.2,
  duration: Math.random() * 2 + 2,
}));

/**
 * RouteAttraction을 MarkerInfo로 변환
 * @param attractions - 여행지 배열
 * @param date - 날짜
 * @returns MarkerInfo 배열
 */
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

/**
 * 일별 경로를 지도에 표시
 * @param date - 선택된 날짜
 * @param attractions - 해당 날짜의 여행지 배열
 */
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

/**
 * 경로 표시 초기화
 */
const resetRoute = () => {
  selectedDate.value = null;

  clearPolylines();

  if (planDetail.value) {
    selectPlanDetail(planDetail.value as PlanDetail);
  }
};

/**
 * 플랜 데이터 초기화
 */
const initializeData = async () => {
  // API에서 플랜 데이터 가져오기
  const data = await getPlanDetail(props.planId);

  planStore.setPlanDetail(data);

  // 지도에 표시
  if (data) {
    selectPlanDetail(data);
  }
};

// planDetail의 변경사항을 지도에 반영 (순서 변경 시 자동 업데이트)
watch(
  () => planDetail.value,
  newPlan => {
    if (newPlan && selectedDate.value && newPlan.details?.[selectedDate.value]) {
      // 현재 선택된 날짜의 경로가 있다면 지도 업데이트
      const attractions = newPlan.details[selectedDate.value];
      showRouteOnMap(selectedDate.value, attractions);
    } else if (newPlan) {
      // 전체 플랜 표시
      selectPlanDetail(newPlan as PlanDetail);
    }
  },
  { deep: true }
);

// planId 변경 감지
watch(
  () => props.planId,
  async (newId, oldId) => {
    if (newId !== oldId) {
      selectedDate.value = null; // 선택된 날짜 초기화
      await initializeData();
    }
  }
);

// 컴포넌트 마운트 시 초기 데이터 로드
onMounted(async () => {
  await initializeData();
});

onUnmounted(() => {
  clearPolylines();
  clearMarkers();
});
</script>
