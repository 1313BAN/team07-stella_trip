<template>
  <section class="relative h-[calc(100vh-4rem)]">
    <ResizablePanelGroup direction="horizontal" class="h-full">
      <!-- 왼쪽 섹션 (필터 + 그리드 또는 상세 정보) -->
      <ResizablePanel :defaultSize="33" :minSize="20" :maxSize="60" class="relative">
        <!-- 라우터 뷰를 통해 자식 컴포넌트(PlanDetail) 렌더링 -->
        <router-view v-slot="{ Component }">
          <template v-if="Component">
            <component :is="Component" />
          </template>
          <template v-else>
            <!-- 기존 Plan 콘텐츠 (필터 + 그리드) -->
            <div
              class="absolute top-0 left-0 z-10 h-fit w-full p-2 transition-transform duration-300"
              :class="{ '-translate-y-full': isScrollingDown && scrollY > 100 }"
            >
              <PlanFilter
                :filters="currentFilters"
                :isScrollingDown="isScrollingDown"
                :scrollY="scrollY"
                @filter="handleFilterChange"
                @clearFilter="clearFilter"
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
                  :filter="currentFilters"
                  :parentScrollContainer="scrollContainerRef"
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
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import MapContainer from '@/components/map/MapContainer.vue';
import MapError from '@/components/map/MapError.vue';
import PlanFilter from '@/components/views/plan/PlanFilter.vue';
import { type PlansParams, type PlansSortOption } from '@/services/api/domains/plan';
import {
  FilteredPlansSkeleton,
  FilteredPlans,
  FilteredPlansError,
} from '@/components/views/plan/FilteredPlans';
import CommonSkeleton from '@/components/skeleton/CommonSkeleton/CommonSkeleton.vue';
import { type Plan, type Tag } from '@/services/api/domains/plan';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import { useMapState } from '@/composables/useMapState';
import { useScroll } from '@/composables/useScroll';
import { ROUTES } from '@/router/routes';
import { postPlanLike, deletePlanLike } from '@/services/api/domains/plan';
import { toast } from 'vue-sonner';
import { useAuthStore } from '@/stores/auth';

// Vue Router
const router = useRouter();
const route = useRoute();

// 상태 관리
const scrollContainerRef = ref<HTMLElement | null>(null);
const { mapRef, mapLevel, mapCenter, selectPlan } = useMapState();
const filterComponentRef = ref<InstanceType<typeof PlanFilter> | null>(null);
const { isScrollingDown, scrollY, handleScroll } = useScroll();
const currentFilters = reactive<PlansParams>({
  page: route.query.page ? Number(route.query.page) || 1 : 1,
  size: route.query.size ? Number(route.query.size) || 100 : 100,
  search: route.query.search ? String(route.query.search) : '',
  maxDuration: route.query.maxDuration ? Number(route.query.maxDuration) || undefined : undefined,
  minDuration: route.query.minDuration ? Number(route.query.minDuration) || undefined : undefined,
  userName: route.query.userName ? String(route.query.userName) : '',
  sort: route.query.sort
    ? (String(route.query.sort) as PlansSortOption)
    : ('recent' as PlansSortOption),
});

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

const handleFilterChange = (filters: PlansParams) => {
  currentFilters.search = filters.search;
  currentFilters.maxDuration = filters.maxDuration;
  currentFilters.minDuration = filters.minDuration;
  currentFilters.userName = filters.userName;
  currentFilters.page = filters.page;
  currentFilters.size = filters.size;
  currentFilters.sort = filters.sort as PlansSortOption;
  router.replace({
    path: ROUTES.PLANS.path,
    query: {
      ...currentFilters,
    },
  });
};

const closeFilterPanel = () => {
  if (filterComponentRef.value) {
    filterComponentRef.value.closeFilterPanel();
  }
};

const isLoggedIn = computed(() => {
  const authStore = useAuthStore();
  return authStore.isAuthenticated;
});

// 이벤트 핸들러
const handlePlanLikeClick = (plan: Plan) => {
  if (!isLoggedIn.value) {
    toast('로그인이 필요한 기능입니다.');
    return;
  }
  if (!plan.isLiked) {
    postPlanLike(plan.planId)
      .then(() => {
        plan.isLiked = true;
        plan.likeCount = (plan.likeCount || 0) + 1; // 좋아요 수 증가
        toast('여행 계획 좋아요 추가 성공', {
          description: `여행 계획 "${plan.title}"에 좋아요가 추가되었습니다.`,
        });
      })
      .catch(() => {
        toast.error('여행 계획 좋아요 추가 실패', {
          description: `여행 계획 "${plan.title}"에 좋아요 추가에 실패했습니다.`,
        });
        plan.isLiked = false; // 실패 시 원래 상태로 되돌리기
      });
  } else {
    deletePlanLike(plan.planId)
      .then(() => {
        plan.isLiked = false;
        plan.likeCount = (plan.likeCount || 0) - 1; // 좋아요 수 감소
        toast('여행 계획 좋아요 제거 성공', {
          description: `여행 계획 "${plan.title}"의 좋아요가 제거되었습니다.`,
        });
      })
      .catch(() => {
        toast.error('여행 계획 좋아요 제거 실패', {
          description: `여행 계획 "${plan.title}"의 좋아요 제거에 실패했습니다.`,
        });
        plan.isLiked = true; // 실패 시 원래 상태로 되돌리기
      });
  }
};

const handlePlanTagClick = (tag: Tag) => {
  currentFilters.search = tag.name;
  currentFilters.maxDuration = undefined;
  currentFilters.minDuration = undefined;
  currentFilters.userName = undefined;
  currentFilters.sort = 'recent';
  router.replace({
    path: ROUTES.PLANS.path,
    query: {
      ...currentFilters,
    },
  });
};

const clearFilter = () => {
  currentFilters.search = undefined;
  currentFilters.maxDuration = undefined;
  currentFilters.minDuration = undefined;
  currentFilters.userName = undefined;
  currentFilters.sort = 'recent';
  router.replace({
    path: ROUTES.PLANS.path,
    query: {
      ...currentFilters,
    },
  });
};
</script>
