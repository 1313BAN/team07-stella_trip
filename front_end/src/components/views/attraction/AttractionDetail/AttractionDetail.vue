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
          <h3 class="font-semibold text-purple-200">리뷰 {{ reviews.length }}개</h3>
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
        <div v-if="reviews && reviews.length > 0" class="space-y-4">
          <div
            v-for="review in reviews"
            :key="review.reviewId"
            class="rounded-lg border border-white/10 bg-gradient-to-br from-slate-900/70 to-purple-900/30 p-4 shadow-sm transition-all duration-200 hover:bg-white/10"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-3">
                <div
                  class="flex h-9 w-9 items-center justify-center rounded-full bg-purple-800/60 text-purple-200 shadow-inner"
                >
                  <User class="h-5 w-5" />
                </div>
                <div>
                  <div class="flex items-center gap-1">
                    <Star
                      v-for="i in 5"
                      :key="i"
                      :class="[
                        'h-4 w-4',
                        i <= review.rating
                          ? 'fill-yellow-400 text-yellow-400 drop-shadow'
                          : 'text-gray-700',
                      ]"
                    />
                  </div>
                  <span class="mt-0.5 block text-xs text-gray-400">{{ review.visitedDate }}</span>
                </div>
              </div>
              <button
                class="flex items-center gap-1 rounded-full px-2 py-1 text-xs font-medium text-purple-200 transition-colors hover:bg-purple-900/30"
                :aria-label="review.isLiked ? '좋아요 취소하기' : '좋아요 추가하기'"
                :aria-pressed="review.isLiked ? 'true' : 'false'"
                @click="toggleReviewLike(review)"
              >
                <ThumbsUp
                  :class="[
                    'h-4 w-4 transition-all',
                    review.isLiked ? 'scale-110 fill-purple-400 text-purple-400' : 'text-gray-400',
                  ]"
                />
                <span>{{ review.likeCount ?? 0 }}</span>
              </button>
            </div>
            <div class="mt-2">
              <h4 class="truncate text-base font-semibold text-white">{{ review.title }}</h4>
              <p class="mt-1 text-sm whitespace-pre-line text-gray-200">{{ review.content }}</p>
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

    <!-- 리뷰 작성 폼 -->
    <Transition name="fade">
      <div
        v-if="isReviewFormOpen"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/80"
      >
        <div class="w-full max-w-md rounded-lg bg-slate-800 p-6 shadow-lg">
          <h3 class="mb-4 text-lg font-semibold text-white">리뷰 작성</h3>
          <div class="mb-4">
            <label for="review-title" class="mb-2 block text-sm font-medium text-gray-300">
              제목
            </label>
            <input
              id="review-title"
              v-model="newReview.title"
              type="text"
              class="w-full rounded-md border border-white/20 bg-slate-900 p-3 text-white placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
              placeholder="리뷰 제목을 입력하세요"
            />
          </div>
          <div class="mb-4">
            <label for="review-content" class="mb-2 block text-sm font-medium text-gray-300">
              내용
            </label>
            <textarea
              id="review-content"
              v-model="newReview.content"
              class="w-full rounded-md border border-white/20 bg-slate-900 p-3 text-white placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
              rows="3"
              placeholder="리뷰 내용을 입력하세요"
            />
          </div>
          <div class="mb-4">
            <label for="visit-date" class="mb-2 block text-sm font-medium text-gray-300">
              방문 일자
            </label>
            <div
              class="flex w-full items-center justify-center rounded-md border border-white/20 bg-transparent p-4"
            >
              <Calendar
                v-model="selectedDate"
                weekdayFormat="short"
                class="mx-auto w-auto rounded-md text-white"
                mode="single"
              />
            </div>
          </div>
          <div class="mb-4">
            <label class="mb-2 block text-sm font-medium text-gray-300">평점</label>
            <div class="flex items-center gap-2">
              <Star
                v-for="i in 5"
                :key="i"
                :class="[
                  'h-5 w-5 cursor-pointer',
                  i <= newReview.rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-700',
                ]"
                @click="newReview.rating = i"
              />
            </div>
          </div>
          <div class="flex justify-end gap-2">
            <Button
              variant="outline"
              class="flex-1 rounded-md border border-white/20 bg-transparent text-white hover:bg-white/10"
              @click="closeReviewForm"
            >
              취소
            </Button>
            <Button
              :disabled="isSubmitting"
              class="flex-1 rounded-md bg-purple-600 py-2 font-medium text-white transition-all duration-200 hover:bg-purple-700"
              @click="submitReview"
            >
              <span v-if="isSubmitting">제출 중...</span>
              <span v-else>제출</span>
            </Button>
          </div>
        </div>
      </div>
    </Transition>

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
import { ref, computed, watch, type Ref } from 'vue';
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
import { Calendar } from '@/components/ui/calendar';
import ImageWithFallback from '@/components/common/ImageWithFallback/ImageWithFallback.vue';
import { getContentTypeNameKR } from '@/utils/util';
import { getAttractionReview, postAttractionReview } from '@/services/api/domains/attraction';
import type { Review, Attraction } from '@/services/api/domains/attraction/types';
import { useAuthStore } from '@/stores/auth';
import { toast } from 'vue-sonner';
import { type DateValue, getLocalTimeZone, today } from '@internationalized/date';
import { postReviewLike, deleteReviewLike } from '@/services/api/domains/attraction';

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
const isReviewFormOpen = ref(false);
const selectedDate = ref(today(getLocalTimeZone())) as Ref<DateValue>;
const newReview = ref({
  title: '',
  content: '',
  rating: 5,
  visit_date: new Date().toISOString().split('T')[0],
});
const isSubmitting = ref(false);

watch(
  () => props.attraction,
  newAttraction => {
    if (!newAttraction) return;
    getAttractionReview(newAttraction.attractionId)
      .then(response => {
        reviews.value = response.content;
      })
      .catch(() => {});
  },
  { immediate: true }
);

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

  if (review.isLiked) {
    review.likeCount = review.likeCount ? review.likeCount + 1 : 1;
    postReviewLike(props.attractionId, review.reviewId)
      .then(() => {
        toast.success('리뷰에 좋아요를 추가했습니다');
      })
      .catch(() => {
        review.isLiked = false; // 실패 시 원래 상태로 복원
        review.likeCount = review.likeCount ? review.likeCount - 1 : 0;
        toast.error('좋아요 추가에 실패했습니다', {
          description: '잠시 후 다시 시도해주세요',
        });
      });
  } else {
    review.likeCount = review.likeCount ? review.likeCount - 1 : 0;
    deleteReviewLike(props.attractionId, review.reviewId)
      .then(() => {
        toast.success('리뷰 좋아요를 취소했습니다');
      })
      .catch(() => {
        review.isLiked = true; // 실패 시 원래 상태로 복원
        review.likeCount = review.likeCount ? review.likeCount + 1 : 1;
        toast.error('좋아요 취소에 실패했습니다', {
          description: '잠시 후 다시 시도해주세요',
        });
      });
  }
};

// 리뷰 작성 폼 열기
const openReviewForm = () => {
  isReviewFormOpen.value = true;
};

// 리뷰 작성 폼 닫기
const closeReviewForm = () => {
  isReviewFormOpen.value = false;
  // 폼 초기화
  newReview.value = {
    title: '',
    content: '',
    rating: 5,
    visit_date: '',
  };
};

// 리뷰 제출
const submitReview = async () => {
  if (!props.attractionId) return;

  // 유효성 검사
  if (!newReview.value.title.trim()) {
    toast.error('제목을 입력해주세요');
    return;
  }

  if (!newReview.value.content.trim()) {
    toast.error('내용을 입력해주세요');
    return;
  }

  isSubmitting.value = true; // API 호출 (실제로는 이 API를 호출해야 함)

  postAttractionReview(props.attractionId, {
    title: newReview.value.title,
    content: newReview.value.content,
    rating: newReview.value.rating,
    visitedDate: newReview.value.visit_date,
  })
    .then(() => {
      toast.success('리뷰가 등록되었습니다');
      closeReviewForm();
      if (!props.attraction) return;
      getAttractionReview(props.attraction.attractionId)
        .then(response => {
          reviews.value = response.content;
        })
        .catch(() => {});
    })
    .catch(() => {
      toast.error('리뷰 등록에 실패했습니다', {
        description: '잠시 후 다시 시도해주세요',
      });
    })
    .finally(() => {
      isSubmitting.value = false;
    });
};

// 여행 계획에 추가
const handleAddToPlan = () => {
  emit('addToPlan', props.attractionId);
};
</script>
