<template>
  <div class="h-full overflow-y-auto bg-slate-950/90 text-white backdrop-blur-md">
    <!-- 헤더 영역 -->
    <div
      class="sticky top-0 z-10 flex items-center justify-between border-b border-white/20 bg-slate-900/80 p-3 backdrop-blur-md"
    >
      <h3 class="truncate text-lg font-semibold text-white">{{ attractionName }}</h3>
      <Button
        variant="ghost"
        size="icon"
        class="h-8 w-8 text-gray-300 hover:bg-white/10 hover:text-white"
        @click="$emit('close')"
      >
        <X class="h-4 w-4" />
        <span class="sr-only">닫기</span>
      </Button>
    </div>

    <!-- 상세 정보 표시 -->
    <div class="flex flex-col divide-y divide-white/10">
      <!-- 메인 이미지 -->
      <div class="relative h-40 w-full">
        <ImageWithFallback
          :src="attractionData?.image ?? ''"
          :alt="attractionData?.name ?? ''"
          class="h-full w-full object-cover"
        />
        <div
          class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent"
        />
        <button
          class="absolute top-2 right-2 rounded-full bg-white/20 p-1.5 text-white shadow-sm transition-all duration-300 hover:bg-white/30"
          :aria-label="attractionData?.isLiked ? '좋아요 취소하기' : '좋아요 추가하기'"
          :aria-pressed="attractionData?.isLiked ? 'true' : 'false'"
          @click="toggleLike"
        >
          <Heart v-if="attractionData?.isLiked" class="h-5 w-5 fill-purple-400 text-purple-400" />
          <Heart v-else class="h-5 w-5 text-gray-300" />
        </button>
      </div>

      <!-- 기본 정보 섹션 -->
      <div class="space-y-3 bg-slate-900/30 p-3">
        <div class="flex items-center gap-1">
          <Badge
            variant="outline"
            class="rounded-md border-purple-400/50 bg-purple-900/20 text-purple-200"
          >
            {{ getAttractionTypeName(attractionData?.contentType ?? 0) }}
          </Badge>
          <div class="ml-1 flex items-center">
            <Star class="h-4 w-4 fill-yellow-400 text-yellow-400" />
            <span class="ml-1 text-sm font-medium text-yellow-200">
              {{ (attractionData?.rating ?? 0).toFixed(1) }}
            </span>
          </div>
        </div>

        <h2 class="text-xl font-bold text-white">{{ attractionData?.name }}</h2>

        <div class="flex items-start gap-2 text-sm text-gray-300">
          <MapPin class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
          <span>{{ attractionData?.address ?? '' }}</span>
        </div>
      </div>

      <!-- 리뷰 섹션 -->
      <div class="flex-1 bg-slate-900/20 p-3">
        <div class="mb-3 flex items-center justify-between">
          <h3 class="font-semibold text-purple-200">
            리뷰 {{ attractionData?.review?.length ?? 0 }}개
          </h3>
          <Button
            size="sm"
            variant="outline"
            class="h-8 border-purple-400/50 bg-purple-900/30 text-purple-200 hover:bg-purple-800/40 hover:text-white"
            @click="openReviewForm"
          >
            <PenLine class="mr-1 h-3 w-3" />
            리뷰 작성
          </Button>
        </div>

        <!-- 리뷰 목록 -->
        <div v-if="attractionData?.review && attractionData.review.length > 0" class="space-y-4">
          <div
            v-for="review in attractionData.review"
            :key="review.reviewId"
            class="rounded-lg border border-white/10 bg-white/5 p-3 shadow-sm transition-all duration-200 hover:bg-white/10"
          >
            <div class="mb-2 flex items-center justify-between">
              <div class="flex items-center gap-2">
                <div
                  class="flex h-8 w-8 items-center justify-center rounded-full bg-purple-900/50 text-purple-200"
                >
                  <User class="h-5 w-5" />
                </div>
                <div>
                  <div class="font-medium text-white">{{ review.userName }}</div>
                  <div class="flex text-xs text-gray-400">
                    <span>{{ review.visitedDate }} 방문</span>
                    <span class="mx-1">•</span>
                    <span>{{ review.createdAt }}</span>
                  </div>
                </div>
              </div>
              <div class="flex items-center">
                <div class="flex">
                  <Star
                    v-for="i in 5"
                    :key="i"
                    :class="[
                      'h-4 w-4',
                      i <= review.rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-700',
                    ]"
                  />
                </div>
              </div>
            </div>
            <h4 class="font-medium text-white">{{ review.title }}</h4>
            <p class="mt-1 text-sm text-gray-300">{{ review.content }}</p>
            <div class="mt-2 flex justify-end">
              <button
                class="flex items-center gap-1 rounded-full p-1.5 text-sm text-gray-300 transition-colors hover:bg-white/10"
                :aria-label="review.isLiked ? '좋아요 취소하기' : '좋아요 추가하기'"
                :aria-pressed="review.isLiked ? 'true' : 'false'"
                @click="toggleReviewLike(review)"
              >
                <ThumbsUp
                  :class="['h-4 w-4', review.isLiked ? 'fill-purple-400 text-purple-400' : '']"
                />
                <span>좋아요</span>
              </button>
            </div>
          </div>
        </div>

        <!-- 리뷰가 없을 경우 -->
        <div
          v-else
          class="flex h-32 flex-col items-center justify-center gap-2 rounded-lg border border-dashed border-white/20 p-4 text-center"
        >
          <MessageSquare class="h-8 w-8 text-purple-400/40" />
          <div class="text-sm text-gray-400">
            <p>아직 리뷰가 없습니다</p>
            <p class="mt-1">첫 번째 리뷰를 남겨보세요!</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { Heart, Star, MapPin, User, PenLine, MessageSquare, ThumbsUp, X } from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import ImageWithFallback from '@/components/common/ImageWithFallback/ImageWithFallback.vue';
import { getAttractionTypeName } from '@/utils/util';
import { getAttractionDetail } from '@/services/api/domains/attraction';
import type { AttractionDetail, Review } from '@/services/api/domains/attraction';

const props = defineProps<{
  attractionId: number;
  attractionName: string;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'toggleLike', attractionId: number): void;
  (e: 'writeReview', attractionId: number): void;
}>();

const attractionData = ref<AttractionDetail | null>(null);

const initialData = await getAttractionDetail(props.attractionId);
attractionData.value = initialData;

const fetchAttractionDetail = async (id: number) => {
  const data = await getAttractionDetail(id);
  attractionData.value = data;
};

watch(
  () => props.attractionId,
  async (newId, oldId) => {
    if (newId !== oldId) {
      await fetchAttractionDetail(newId);
    }
  }
);

// 좋아요 토글
const toggleLike = async () => {
  if (!attractionData.value) return;

  // 로컬 상태 업데이트
  attractionData.value.isLiked = !attractionData.value.isLiked;

  // API 호출 및 이벤트 발생
  emit('toggleLike', props.attractionId);

  // 실제 API 호출 코드 (예시)
  // await likeAttraction(props.attractionId);
};

// 리뷰 좋아요 토글
const toggleReviewLike = async (review: Review) => {
  // 로컬 상태 업데이트
  review.isLiked = !review.isLiked;

  // 실제 API 호출 코드 (예시)
  // await likeReview(review.reviewId);
};

// 리뷰 작성 폼 열기
const openReviewForm = () => {
  emit('writeReview', props.attractionId);
};
</script>
