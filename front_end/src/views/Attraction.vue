<template>
  <HeroBackground />
  <div class="h-screen">
    <NavigationBar />
    <section class="relative h-[calc(100vh-4rem)]">
      <ResizablePanelGroup direction="horizontal" class="h-full">
        <!-- 왼쪽 섹션 (필터 + 그리드) -->
        <ResizablePanel :defaultSize="33" :minSize="20" :maxSize="60" class="relative">
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
        </ResizablePanel>

        <ResizableHandle withHandle />

        <!-- 오른쪽: 지도 -->
        <ResizablePanel :defaultSize="67" class="relative" @resizeend="handleMapResize">
          <AsyncContainer :loadingComponent="CommonSkeleton" :errorComponent="MapError">
            <MapContainer
              ref="mapRef"
              :center="mapCenter"
              :level="mapLevel"
              :showCenterMarker="true"
            />
          </AsyncContainer>

          <!-- 여행지 상세 정보 영역 (오버레이로 표시) -->
          <div
            v-if="selectedAttractionRef"
            class="absolute top-0 left-0 z-20 w-1/2 max-w-md rounded-md shadow-lg backdrop-blur-sm transition-all duration-300"
            style="height: calc(100% - 2rem); margin: 1rem"
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
        </ResizablePanel>
      </ResizablePanelGroup>
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
} from '@/components/views/attraction/AttractionDetail';
import CommonSkeleton from '@/components/skeleton/CommonSkeleton/CommonSkeleton.vue';
import type { Attraction } from '@/services/api/domains/attraction';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { useMapState } from '@/composables/useMapState';
import { useScroll } from '@/composables/useScroll';
import { useAttractionFilter } from '@/composables/useAttractionFilter';

const scrollContainerRef = ref<HTMLElement | null>(null);
const { mapRef, mapLevel, mapCenter, selectedAttraction, selectAttraction } = useMapState();
const { filterComponentRef, handleFilterChange, closeFilterPanel } = useAttractionFilter();
const { isScrollingDown, scrollY, handleScroll } = useScroll();

// 지도 리사이즈 핸들러
const handleMapResize = () => {
  if (mapRef.value) {
    mapRef.value.refreshMap();
  }
};

// 선택된 여행지 상태 관리
const selectedAttractionRef = ref<Attraction | null>(selectedAttraction.value ?? null);

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
