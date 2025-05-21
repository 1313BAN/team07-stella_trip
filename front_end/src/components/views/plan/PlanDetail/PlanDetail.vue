<template>
  <div class="flex flex-col divide-y divide-white/10">
    <!-- 별자리 표시 섹션 -->
    <div class="relative h-40 w-full bg-indigo-950">
      <!-- 별자리가 있으면 표시, 없으면 기본 배경 -->
      <div v-if="planDetail?.stella" class="h-full w-full">
        <Constellation
          :stars="planDetail.stella.stars"
          :connections="planDetail.stella.connections"
          :backgroundStars="generateBackgroundStars()"
          :isHovered="false"
        />
      </div>
      <div
        v-else
        class="absolute inset-0 bg-gradient-to-br from-slate-900 via-purple-950 to-indigo-950 opacity-70"
      ></div>
      <div
        class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent"
      ></div>

      <!-- 좋아요 버튼 -->
      <button
        class="absolute top-2 right-2 rounded-full bg-white/20 p-1.5 text-white shadow-sm transition-all duration-300 hover:bg-white/30"
        :aria-label="planDetail?.isLiked ? '좋아요 취소하기' : '좋아요 추가하기'"
        :aria-pressed="planDetail?.isLiked ? 'true' : 'false'"
        @click="toggleLike"
      >
        <Heart v-if="planDetail?.isLiked" class="h-5 w-5 fill-purple-400 text-purple-400" />
        <Heart v-else class="h-5 w-5 text-gray-300" />
      </button>
    </div>

    <!-- 기본 정보 섹션 -->
    <div class="space-y-3 bg-slate-900/30 p-3">
      <div class="flex items-center justify-between">
        <div class="flex flex-wrap gap-2">
          <Badge
            v-for="tag in planDetail?.tags"
            :key="tag.tagId"
            variant="outline"
            class="rounded-md border-purple-400/50 bg-purple-900/20 text-purple-200"
          >
            {{ tag.name || `태그 ${tag.tagId}` }}
          </Badge>
        </div>
        <div class="flex items-center">
          <Heart class="h-4 w-4 fill-purple-400 text-purple-400" />
          <span class="ml-1 text-sm font-medium text-purple-200">
            {{ planDetail?.likeCount || 0 }}
          </span>
        </div>
      </div>

      <h2 class="text-xl font-bold text-white">{{ planDetail?.title }}</h2>

      <div class="flex items-start gap-2 text-sm text-gray-300">
        <Calendar class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
        <span>{{ formatDateRange(planDetail?.startDate, planDetail?.endDate) }}</span>
      </div>

      <div v-if="planDetail?.description" class="mt-2 text-sm text-gray-300">
        {{ planDetail.description }}
      </div>

      <div class="flex items-start gap-2 text-sm text-gray-300">
        <Users class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
        <div class="flex flex-wrap gap-1">
          <span
            v-for="writer in planDetail?.planWriters"
            :key="writer.userId"
            class="inline-block rounded-full bg-purple-900/30 px-2 py-0.5"
          >
            {{ writer.name }}
          </span>
        </div>
      </div>
    </div>

    <!-- 일정 섹션 -->
    <div class="flex-1 bg-slate-900/20 p-3">
      <div class="mb-3">
        <h3 class="font-semibold text-purple-200">일정</h3>
      </div>

      <!-- 일별 일정 목록 -->
      <div v-if="planDetail?.details" class="space-y-4">
        <div
          v-for="(attractions, date) in planDetail.details"
          :key="date"
          class="rounded-lg border border-white/10 bg-white/5 p-3"
        >
          <h4
            class="mb-2 flex items-center border-b border-white/10 pb-2 text-lg font-semibold text-white"
          >
            <Calendar class="mr-2 h-5 w-5 text-purple-400" />
            {{ date }}
          </h4>

          <!-- 해당 날짜의 일정 -->
          <div class="space-y-3">
            <div
              v-for="attraction in sortAttractionsByOrder(attractions)"
              :key="attraction.routeId"
              class="flex gap-3 rounded-lg p-2 transition-colors hover:bg-white/5"
            >
              <div
                class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-purple-900/50 text-purple-200"
              >
                {{ attraction.order }}
              </div>

              <div class="flex-1">
                <div class="mb-1 flex items-center justify-between">
                  <h5 class="font-medium text-white">{{ attraction.name }}</h5>
                  <span class="text-sm text-purple-300">
                    {{ formatTime(attraction.visitTime) }}
                  </span>
                </div>

                <div class="flex items-start gap-1 text-xs text-gray-400">
                  <MapPin class="mt-0.5 h-3 w-3 flex-shrink-0 text-purple-400" />
                  <span class="line-clamp-1">{{ attraction.address }}</span>
                </div>

                <div v-if="attraction.memo" class="mt-1 text-sm text-gray-300">
                  <span class="line-clamp-2">{{ attraction.memo }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 일정이 없을 경우 -->
      <div
        v-else
        class="flex h-32 flex-col items-center justify-center gap-2 rounded-lg border border-dashed border-white/20 p-4 text-center"
      >
        <ClipboardList class="h-8 w-8 text-purple-400/40" />
        <div class="text-sm text-gray-400">
          <p>아직 일정이 없습니다</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Heart, Calendar, MapPin, Users, ClipboardList } from 'lucide-vue-next';
import { Badge } from '@/components/ui/badge';
import Constellation from '@/components/common/Constellation/Constellation.vue';
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
const { selectPlanDetail } = useMapState();
const planDetail = ref<PlanDetail | null>(null);

// 날짜 범위 표시 형식
const formatDateRange = (startDate?: string, endDate?: string) => {
  if (!startDate || !endDate) return '';

  const start = new Date(startDate);
  const end = new Date(endDate);

  const startStr = `${start.getFullYear()}년 ${start.getMonth() + 1}월 ${start.getDate()}일`;
  const endStr = `${end.getFullYear()}년 ${end.getMonth() + 1}월 ${end.getDate()}일`;

  return `${startStr} ~ ${endStr}`;
};

// 시간 포맷팅
const formatTime = (timeStr: string) => {
  if (!timeStr) return '';

  const [hours, minutes] = timeStr.split(':');
  return `${hours}:${minutes}`;
};

// 여행지 순서별 정렬
const sortAttractionsByOrder = (attractions: RouteAttraction[]) => {
  if (!attractions || !Array.isArray(attractions)) return [];
  return [...attractions].sort((a, b) => a.order - b.order);
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

// AsyncContainer와 Suspense와 함께 사용하기 위해 setup에서 직접 호출
await initializeData();
</script>
