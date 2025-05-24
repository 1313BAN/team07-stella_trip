<template>
  <section class="relative flex h-full flex-col">
    <!-- 헤더 -->
    <div class="flex-shrink-0 border-b border-white/10 bg-slate-900/50 p-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-3">
          <Button
            variant="ghost"
            size="sm"
            class="text-purple-200 hover:bg-purple-900/30"
            @click="goBack"
          >
            <ArrowLeft class="h-4 w-4" />
            뒤로가기
          </Button>
          <div class="h-6 w-px bg-white/20"></div>
          <h1 class="text-lg font-semibold text-white">여행지 검색</h1>
        </div>
        <Badge
          v-if="currentDate"
          variant="outline"
          class="border-purple-400/30 bg-purple-900/30 text-purple-200"
        >
          {{ currentDate }} 일정에 추가
        </Badge>
      </div>
    </div>

    <!-- 메인 컨텐츠 -->
    <div class="relative flex-1 overflow-hidden">
      <!-- 필터 -->
      <div
        class="absolute top-0 left-0 z-10 h-fit w-full p-2 transition-transform duration-300"
        :class="{ '-translate-y-full': isScrollingDown && scrollY > 100 }"
      >
        <AttractionFilter
          ref="filterComponentRef"
          :isScrollingDown="isScrollingDown"
          :scrollY="scrollY"
          @filter="handleFilterChange"
        />
      </div>

      <!-- 그리드 영역 (스크롤 가능) -->
      <div
        ref="scrollContainerRef"
        class="h-full overflow-x-hidden overflow-y-auto px-2 pt-12"
        @scroll="e => handleScroll(e, closeFilterPanel)"
      >
        <AsyncContainer
          :loadingComponent="FilteredAttractionsSkeleton"
          :errorComponent="FilteredAttractionsError"
        >
          <FilteredAttractions
            :parentScrollContainer="scrollContainerRef"
            :apiParams="{ page: 1, size: 100 }"
            :showAddButton="true"
            @cardClick="handleAttractionCardClick"
            @addClick="handleAddClick"
            @likeClick="handleAttractionLikeClick"
            @tagClick="handleAttractionTagClick"
            @moreClick="() => handleMoreClick(12)"
          />
        </AsyncContainer>
      </div>
    </div>
  </section>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ArrowLeft } from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import {
  FilteredAttractionsSkeleton,
  FilteredAttractions,
  FilteredAttractionsError,
} from '@/components/views/attraction/FilteredAttractions';
import AttractionFilter from '@/components/views/attraction/AttractionFilter.vue';
import type { Attraction } from '@/services/api/domains/attraction';
import { useScroll } from '@/composables/useScroll';
import { useAttractionFilter } from '@/composables/useAttractionFilter';
import { useMapState } from '@/composables/useMapState';
import { toast } from 'vue-sonner';

const props = defineProps<{
  currentDate?: string | null;
}>();

// Emits
const emit = defineEmits<{
  addAttraction: [attraction: Attraction, date: string];
}>();

const router = useRouter();
const route = useRoute();

// 쿼리에서 날짜 가져오기
const selectedDate = computed(() => props.currentDate || (route.query.date as string) || null);

const scrollContainerRef = ref<HTMLElement | null>(null);
const { filterComponentRef, handleFilterChange, closeFilterPanel } = useAttractionFilter();
const { isScrollingDown, scrollY, handleScroll } = useScroll();
const { selectAttraction } = useMapState();

// 뒤로가기
const goBack = () => {
  router.back();
};

// 여행지 카드 클릭 핸들러 (상세 정보 표시용)
const handleAttractionCardClick = (attraction: Attraction) => {
  console.log('여행지 카드 클릭:', attraction.name);
  selectAttraction(attraction);
};

// 여행지 추가 버튼 클릭 핸들러
const handleAddClick = (attraction: Attraction) => {
  if (!selectedDate.value) {
    toast.error('날짜를 선택해주세요', {
      description: '관광지를 추가하려면 먼저 날짜를 선택해야 합니다.',
    });
    return;
  }

  emit('addAttraction', attraction, selectedDate.value);
};

// 기타 이벤트 핸들러들
const handleAttractionLikeClick = (attraction: Attraction) => {
  console.log('관광지 좋아요 클릭:', attraction.name);
};

const handleAttractionTagClick = (contentType: number) => {
  console.log('관광지 태그(컨텐츠 타입) 클릭:', contentType);
};

const handleMoreClick = (contentType: number) => {
  console.log('더보기 클릭:', contentType);
};
</script>
