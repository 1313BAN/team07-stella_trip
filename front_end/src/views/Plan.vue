<template>
  <HeroBackground />
  <div class="h-screen">
    <NavigationBar />
    <section class="relative h-[calc(100vh-4rem)]">
      <ResizablePanelGroup direction="horizontal" class="h-full">
        <!-- 왼쪽 섹션 (필터 + 그리드 또는 상세 정보) -->
        <ResizablePanel :defaultSize="33" :minSize="20" :maxSize="60" class="relative">
          <!-- 라우터 뷰를 통해 자식 컴포넌트(PlanDetail) 렌더링 -->
          <router-view v-slot="{ Component }">
            <template v-if="Component">
              <component :is="Component" :mapRef="mapRef" />
            </template>
            <template v-else>
              <!-- 기존 Plan 콘텐츠 (필터 + 그리드) -->
              <div
                class="absolute top-0 left-0 z-10 h-fit w-full p-2 transition-transform duration-300"
                :class="{ '-translate-y-full': isScrollingDown && scrollY > 100 }"
              >
                <PlanFilter
                  ref="filterComponentRef"
                  :isScrollingDown="isScrollingDown"
                  :scrollY="scrollY"
                  @filter="handleFilterChange"
                />
              </div>
              <div
                ref="scrollContainerRef"
                class="h-full overflow-x-hidden overflow-y-auto px-2 pt-12"
                @scroll="e => handleScroll(e, closeFilterPanel)"
              >
                <AsyncContainer
                  :loadingComponent="FilteredPlansSkeleton"
                  :errorComponent="FilteredPlansError"
                >
                  <FilteredPlans
                    :parentScrollContainer="scrollContainerRef"
                    :apiParams="{ page: 1, size: 100 }"
                    @cardClick="handlePlanCardClick"
                    @likeClick="handlePlanLikeClick"
                    @tagClick="handlePlanTagClick"
                  />
                </AsyncContainer>
              </div>
            </template>
          </router-view>
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
        </ResizablePanel>
      </ResizablePanelGroup>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import HeroBackground from '@/components/background/HeroBackground.vue';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import MapContainer from '@/components/map/MapContainer.vue';
import MapError from '@/components/map/MapError.vue';
import {
  FilteredPlansSkeleton,
  FilteredPlans,
  FilteredPlansError,
} from '@/components/views/plan/FilteredPlans';
import PlanFilter from '@/components/views/plan/PlanFilter.vue';
import NavigationBar from '@/components/NavigationBar/NavigationBar.vue';
import CommonSkeleton from '@/components/skeleton/CommonSkeleton/CommonSkeleton.vue';
import { type Plan, type Tag } from '@/services/api/domains/plan';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { useMapState } from '@/composables/useMapState';
import { useScroll } from '@/composables/useScroll';
import { usePlanFilter } from '@/composables/usePlanFilter';
import { ROUTES } from '@/router/routes';

// Vue Router
const router = useRouter();

// 상태 관리
const scrollContainerRef = ref<HTMLElement | null>(null);
const { mapRef, mapLevel, mapCenter, selectPlan } = useMapState();
const { filterComponentRef, handleFilterChange, closeFilterPanel } = usePlanFilter();
const { isScrollingDown, scrollY, handleScroll } = useScroll();

// 지도 리사이즈 핸들러
const handleMapResize = () => {
  if (mapRef.value?.refreshMap) {
    mapRef.value.refreshMap();
  }
};

// 여행 계획 카드 클릭 핸들러
const handlePlanCardClick = (plan: Plan) => {
  // 기본 정보 설정 (지도 표시용)
  selectPlan(plan);

  // 선택한 계획의 상세 페이지로 이동
  router.push({
    name: ROUTES.PLAN_DETAIL.name,
    params: { planId: plan.planId.toString() },
  });
};

// 이벤트 핸들러
const handlePlanLikeClick = (plan: Plan) => {
  console.log('여행 계획 좋아요 클릭:', plan.title);
};

const handlePlanTagClick = (tag: Tag) => {
  console.log('여행 계획 태그 클릭:', tag.name);
};
</script>
