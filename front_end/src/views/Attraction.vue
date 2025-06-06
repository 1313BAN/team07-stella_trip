<template>
  <section class="relative h-[calc(100vh-4rem)]">
    <ResizablePanelGroup direction="horizontal" class="h-full">
      <!-- 왼쪽 섹션 (필터 + 그리드) -->
      <ResizablePanel :defaultSize="33" :minSize="20" :maxSize="60" class="relative">
        <div
          class="absolute top-0 left-0 z-10 h-fit w-full p-2 transition-transform duration-300"
          :class="{ '-translate-y-full': isScrollingDown && scrollY > 100 }"
        >
          <AttractionFilter
            :filterParams="filterParams"
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
              :attractions="attractions"
              :parentScrollContainer="scrollContainerRef"
              :apiParams="filterParams"
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
            :showCenterMarker="false"
          />
        </AsyncContainer>

        <!-- 여행지 상세 정보 영역 (오버레이로 표시) -->
        <div
          v-if="selectedAttractionRef"
          class="absolute top-0 left-0 z-20 m-1 h-full w-1/2 max-w-md rounded-md shadow-lg backdrop-blur-sm transition-all duration-300"
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
              @toggleLike="handleAttractionLikeClick"
              @reloadCards="fetchAttractions"
            />
          </AsyncContainer>
        </div>
      </ResizablePanel>
    </ResizablePanelGroup>
  </section>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import MapContainer from '@/components/map/MapContainer.vue';
import MapError from '@/components/map/MapError.vue';
import {
  FilteredAttractionsSkeleton,
  FilteredAttractions,
  FilteredAttractionsError,
} from '@/components/views/attraction/FilteredAttractions';
import AttractionFilter from '@/components/views/attraction/AttractionFilter.vue';
import {
  AttractionDetail,
  AttractionDetailError,
  AttractionDetailSkeleton,
} from '@/components/views/attraction/AttractionDetail';
import CommonSkeleton from '@/components/skeleton/CommonSkeleton/CommonSkeleton.vue';
import type { Attraction } from '@/services/api/domains/attraction/types';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { useMapState } from '@/composables/useMapState';
import { useScroll } from '@/composables/useScroll';
import type { AttractionsParams } from '@/services/api/domains/attraction/types';
import { getAttractions } from '@/services/api/domains/attraction';
import { postAttractionLike, deleteAttractionLike } from '@/services/api/domains/attraction/index';
import { toast } from 'vue-sonner';
import { useAuthStore } from '@/stores/auth';
import { useRoute } from 'vue-router';
import { ROUTES } from '@/router/routes';
import router from '@/router';

const route = useRoute();
const authStore = useAuthStore();

const scrollContainerRef = ref<HTMLElement | null>(null);
const { mapRef, mapLevel, mapCenter, selectedAttraction, selectAttraction, clearMarkers } =
  useMapState();
const { isScrollingDown, scrollY, handleScroll } = useScroll();
const filterParams = reactive<AttractionsParams>({
  page: route.query.page ? Number(route.query.page) || 1 : 1,
  size: route.query.size ? Number(route.query.size) || 100 : 100,
  sidoCode: route.query.sidoCode ? Number(route.query.sidoCode) : undefined,
  gugunCode: route.query.gugunCode ? Number(route.query.gugunCode) : undefined,
  contentTypeIds: route.query.contentTypeIds
    ? (Array.isArray(route.query.contentTypeIds)
        ? route.query.contentTypeIds.map(Number)
        : [Number(route.query.contentTypeIds)]) || []
    : [],
  keyword: route.query.keyword ? String(route.query.keyword) : '',
});

// 지도 리사이즈 핸들러
const handleMapResize = () => {
  if (mapRef.value) {
    mapRef.value.refreshMap();
  }
};

onMounted(async () => {
  selectedAttraction.value = null; // 초기화
  clearMarkers();
  await fetchAttractions();
  if (route.query.selectedAttractionId) {
    const attractionId = Number(route.query.selectedAttractionId);
    if (attractionId) {
      const attraction = attractions.value.find(a => a.attractionId === attractionId);
      if (attraction) {
        selectAttraction(attraction);
        selectedAttractionRef.value = attraction;
      }
    }
  }
});

watch(
  () => authStore.isAuthenticated,
  () => {
    fetchAttractions();
  }
);

// 선택된 여행지 상태 관리
const selectedAttractionRef = ref<Attraction | null>(selectedAttraction.value ?? null);
const filterComponentRef = ref<InstanceType<typeof AttractionFilter> | null>(null);

const attractions = ref<Attraction[]>([]);

const fetchAttractions = async () => {
  const response = await getAttractions(filterParams);
  attractions.value = response.content;
};

// 관광지 카드 클릭 핸들러
const handleAttractionCardClick = (attraction: Attraction) => {
  selectAttraction(attraction);
  selectedAttractionRef.value = attraction;
  router.replace({
    path: ROUTES.ATTRACTION.path,
    query: {
      ...filterParams,
      selectedAttractionId: attraction.attractionId.toString(),
    },
  });

  // 별도의 라우팅 없이 현재 화면에서 상세 정보 표시
  // 라우터로 인한 컴포넌트 리셋 방지
};

// 리뷰 닫기 핸들러
const closeReview = () => {
  selectedAttractionRef.value = null;
  clearMarkers();
  router.replace({
    path: ROUTES.ATTRACTION.path,
    query: {
      ...filterParams,
      selectedAttractionId: undefined,
    },
  });
};

const closeFilterPanel = () => {
  if (filterComponentRef.value) {
    filterComponentRef.value.closeFilterPanel();
  }
};

const handleFilterChange = (filters: AttractionsParams) => {
  // 필터 변경 시 처리 로직
  filterParams.sidoCode = filters.sidoCode;
  filterParams.gugunCode = filters.gugunCode;
  filterParams.contentTypeIds = filters.contentTypeIds;
  filterParams.keyword = filters.keyword;
  filterParams.page = filters.page;
  filterParams.size = filters.size;
  router.replace({
    path: ROUTES.ATTRACTION.path,
    query: {
      ...filterParams,
    },
  });
  fetchAttractions();
};

// 남은 이벤트 핸들러들
const handleAttractionLikeClick = (attraction: Attraction) => {
  if (!authStore.isAuthenticated) {
    toast('로그인이 필요합니다.');
    return;
  }

  if (attraction.isLiked) {
    attraction.isLiked = false;
    attraction.likeCount = attraction.likeCount == 0 ? 0 : attraction.likeCount - 1;
    deleteAttractionLike(attraction.attractionId)
      .then(() => {
        toast('관광지 좋아요 취소 성공');
      })
      .catch(() => {
        toast('관광지 좋아요 취소 실패');
        // 원래 상태로 롤백
        attraction.isLiked = !attraction.isLiked;
        attraction.likeCount = attraction.isLiked
          ? attraction.likeCount + 1
          : attraction.likeCount - 1;
      });
  } else {
    attraction.isLiked = true;
    attraction.likeCount += 1;
    // API 호출 및 상태 업데이트
    postAttractionLike(attraction.attractionId)
      .then(() => {
        toast('관광지 좋아요 상태 업데이트 성공');
      })
      .catch(() => {
        toast('관광지 좋아요 상태 업데이트 실패:');
        // 원래 상태로 롤백
        attraction.isLiked = !attraction.isLiked;
        attraction.likeCount = attraction.isLiked
          ? attraction.likeCount + 1
          : attraction.likeCount - 1;
      });
  }
};

const handleAttractionTagClick = (contentType: number) => {
  filterParams.contentTypeIds = [contentType];
  filterParams.page = 1;
  filterParams.size = 100;
  filterParams.keyword = '';
  filterParams.sidoCode = undefined;
  filterParams.gugunCode = undefined;
  router.replace({
    path: ROUTES.ATTRACTION.path,
    query: {
      ...filterParams,
    },
  });
  fetchAttractions();
};

const handleMoreClick = (contentType: number) => {
  console.log('더보기 클릭:', contentType);
};
</script>
