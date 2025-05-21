<template>
  <HeroBackground />
  <div class="h-screen">
    <NavigationBar />
    <section class="relative flex flex-col lg:flex-row">
      <!-- 왼쪽 섹션 (필터 + 그리드) -->
      <div class="relative w-full lg:w-1/3">
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
          class="h-[calc(100vh-4rem)] overflow-x-hidden overflow-y-auto px-2 pt-12"
          @scroll="e => handleScroll(e, closeFilterPanel)"
        >
          <AsyncContainer
            :loadingComponent="FilteredAttractionsSkeleton"
            :errorComponent="AttractionsError"
          >
            <FilteredAttractions
              :parentScrollContainer="scrollContainerRef"
              :apiParams="{ page: 1, size: 100 }"
              @cardClick="handleAttractionCardClick"
              @likeClick="handleAttractionLikeClick"
              @tagClick="handleAttractionTagClick"
              @moreClick="() => handleMoreClick(12)"
            />
          </AsyncContainer>
        </div>
      </div>

      <!-- 여행지 상세 정보 영역 -->
      <div
        v-if="selectedAttractionRef"
        class="fixed top-16 left-1/3 z-20 w-1/5 rounded-md shadow-lg backdrop-blur-sm transition-all duration-300"
        style="height: calc(100vh - 4rem)"
      >
        <AsyncContainer
          :loadingComponent="AttractionDetailSkeleton"
          :errorComponent="AttractionDetailError"
          :attractionName="selectedAttractionRef.name"
        >
          <AttractionDetail
            :attractionId="selectedAttractionRef.attractionId"
            :attractionName="selectedAttractionRef.name"
            :attraction="selectedAttractionRef"
            @close="closeReview"
          />
        </AsyncContainer>
      </div>

      <!-- 오른쪽: 지도 (2/3) -->
      <div class="w-full lg:mt-0 lg:w-2/3">
        <div class="relative h-full">
          <AsyncContainer :loadingComponent="CommonSkeleton" :errorComponent="MapError">
            <MapContainer
              ref="mapRef"
              :center="mapCenter"
              :level="mapLevel"
              :showCenterMarker="true"
            />
          </AsyncContainer>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import HeroBackground from '@/components/background/HeroBackground.vue';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import MapContainer from '@/components/map/MapContainer.vue';
import MapError from '@/components/map/MapError.vue';
import { AttractionsError } from '@/components/views/main/AttractionsSection';
import {
  FilteredAttractionsSkeleton,
  FilteredAttractions,
} from '@/components/views/attraction/FilteredAttractions';
import AttractionFilter from '@/components/views/attraction/AttractionFilter.vue';
import NavigationBar from '@/components/NavigationBar/NavigationBar.vue';
import {
  AttractionDetail,
  AttractionDetailError,
  AttractionDetailSkeleton,
} from '@/components/views/attraction/AttractonDetail';
import CommonSkeleton from '@/components/skeleton/CommonSkeleton/CommonSkeleton.vue';
import type { Attraction } from '@/services/api/domains/attraction';

import { useMapState } from '@/composables/useMapState';
import { useScroll } from '@/composables/useScroll';
import { useAttractionFilter } from '@/composables/useAttractionFilter';

const scrollContainerRef = ref<HTMLElement | null>(null);
const { mapRef, mapLevel, mapCenter, selectedAttraction, selectAttraction } = useMapState();
const { filterComponentRef, handleFilterChange, closeFilterPanel } = useAttractionFilter();
const { isScrollingDown, scrollY, handleScroll } = useScroll();

// 선택된 여행지 상태 관리
const selectedAttractionRef = ref<Attraction | null>(selectedAttraction.value);

// 관광지 카드 클릭 핸들러
const handleAttractionCardClick = (attraction: Attraction) => {
  selectAttraction(attraction);
  selectedAttractionRef.value = attraction;
};

// 리뷰 닫기 핸들러
const closeReview = () => {
  selectedAttractionRef.value = null;
};

// 남은 이벤트 핸들러들
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
