<template>
  <div class="flex h-full flex-col bg-slate-950/90 text-white backdrop-blur-md">
    <!-- 헤더 영역 -->
    <div
      class="sticky top-0 flex items-center justify-between border-b border-white/20 bg-slate-900/80 p-3 backdrop-blur-md"
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
          :src="props.attraction?.image ?? ''"
          :alt="props.attraction?.name ?? ''"
          class="h-full w-full object-cover"
        />
        <div
          class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent"
        />
        <button
          v-if="isLoggedIn"
          class="absolute top-2 right-2 rounded-full bg-white/20 p-1.5 text-white shadow-sm transition-all duration-300 hover:bg-white/30"
          :aria-label="props.attraction?.isLiked ? '좋아요 취소하기' : '좋아요 추가하기'"
          :aria-pressed="props.attraction?.isLiked ? 'true' : 'false'"
          @click="toggleLike"
        >
          <Heart v-if="props.attraction?.isLiked" class="h-5 w-5 fill-purple-400 text-purple-400" />
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
            {{ getContentTypeNameKR(props.attraction?.contentType ?? null) }}
          </Badge>
          <div class="ml-1 flex items-center">
            <Star class="h-4 w-4 fill-yellow-400 text-yellow-400" />
            <span class="ml-1 text-sm font-medium text-yellow-200">
              {{ (props.attraction?.rating ?? 0).toFixed(1) }}
            </span>
          </div>
        </div>

        <h2 class="text-xl font-bold text-white">{{ props.attraction?.name }}</h2>

        <div class="flex items-start gap-2 text-sm text-gray-300">
          <MapPin class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
          <span>{{ props.attraction?.address ?? '' }}</span>
        </div>
      </div>

      <!-- 리뷰 섹션 -->
      <div class="flex-1 bg-slate-900/20 p-3">
        <div class="mb-3 flex items-center justify-between">
          <h3 class="font-semibold text-purple-200">
            리뷰 {{ props.attraction?.review?.length ?? 0 }}개
          </h3>
          <Button
            v-if="isLoggedIn"
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
        <div
          v-if="props.attraction?.review && props.attraction.review.length > 0"
          class="space-y-4"
        >
          <div
            v-for="review in reviews"
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

    <!-- 하단 여행지 추가하기 버튼 -->
    <div class="flex-shrink-0 border-t border-white/20 bg-slate-900/80 p-4 backdrop-blur-md">
      <Button
        class="w-full rounded-lg bg-purple-600 py-3 font-medium text-white transition-all duration-200 hover:bg-purple-700"
        @click="handleAddToPlan"
      >
        <Plus class="mr-2 h-4 w-4" />
        여행 계획에 추가
      </Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import {
  Heart,
  Star,
  MapPin,
  User,
  PenLine,
  MessageSquare,
  ThumbsUp,
  X,
  Plus,
} from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import ImageWithFallback from '@/components/common/ImageWithFallback/ImageWithFallback.vue';
import { getContentTypeNameKR } from '@/utils/util';
import { getAttractionReview } from '@/services/api/domains/attraction';
import type { Review, Attraction } from '@/services/api/domains/attraction/types';
import { useAuthStore } from '@/stores/auth';

const props = defineProps<{
  attractionId: number;
  attractionName: string;
  attraction?: Attraction | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'toggleLike', attraction: Attraction): void;
  (e: 'writeReview', attractionId: number): void;
  (e: 'addToPlan', attractionId: number): void;
}>();

const isLoggedIn = computed(() => {
  const authStore = useAuthStore();
  return authStore.isAuthenticated;
});

const reviews = ref<Review[]>([]);

onMounted(() => {
  getAttractionReview(props.attractionId)
    .then(response => {
      reviews.value = response.content;
    })
    .catch(() => {});
});

// 좋아요 토글
const toggleLike = async () => {
  if (!props.attraction) return;

  // 로컬 상태 업데이트

  // API 호출 및 이벤트 발생
  if (props.attraction) {
    emit('toggleLike', props.attraction);
  }
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

// 여행 계획에 추가
const handleAddToPlan = () => {
  emit('addToPlan', props.attractionId);
};
</script>
