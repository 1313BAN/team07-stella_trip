<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { toast } from 'vue-sonner';
import { Badge } from '@/components/ui/badge';
import MainConstellationCard from '@/components/MainConstellationCard/MainConstellationCard.vue';
import { getSharedData } from '@/services/api/domains/stella';
import { HeroBackground } from '@/components/background';
import { useConstellationAnimation } from '@/composables/useConstellationAnimation';
import type { PlanDetail } from '@/services/api/domains/plan';

const route = useRoute();
const router = useRouter();

// 상태 관리
const isLoading = ref(true);
const hasError = ref(false);
const sharedPlan = ref<PlanDetail | null>(null);

// 링크 파라미터
const shareLink = computed(() => route.params.link as string);

const {
  showOverlay,
  transformStyle,
  constellationRef,
  containerRef,
  startAnimation,
  constellationClasses,
} = useConstellationAnimation({
  duration: 4000,
  loadDelay: 200,
  transitionDelay: 100,
});

/**
 * 공유된 계획 데이터 로드
 */
const loadSharedPlan = async (): Promise<void> => {
  try {
    isLoading.value = true;
    hasError.value = false;

    const response = await getSharedData(shareLink.value);

    // JSON 문자열을 파싱하여 PlanDetail 객체로 변환
    const planData = JSON.parse(response.stellaData) as PlanDetail;
    sharedPlan.value = planData;

    // 데이터 로딩 완료 후 애니메이션 시작
    await startAnimation();
  } catch (error) {
    console.error('공유된 계획 로드 실패:', error);
    hasError.value = true;
    toast.error('공유된 여행 계획을 불러올 수 없습니다', {
      description: '링크가 유효하지 않거나 만료되었을 수 있습니다.',
      duration: 4000,
    });
  } finally {
    isLoading.value = false;
  }
};

// 컴포넌트 마운트 시 데이터 로드
onMounted(loadSharedPlan);
</script>

<template>
  <div class="h-full overflow-y-auto">
    <HeroBackground />
    <!-- 애니메이션 섹션만 표시 -->
    <section
      class="relative flex min-h-screen items-center justify-center px-4 py-10 sm:px-6 lg:px-8"
    >
      <!-- 배경 오버레이 -->
      <div
        class="bg-opacity-90 fixed inset-0 z-50 bg-black transition-opacity duration-1000"
        :class="{ 'opacity-100': showOverlay, 'pointer-events-none opacity-0': !showOverlay }"
      ></div>

      <div class="relative mx-auto max-w-4xl text-center">
        <!-- 제목 -->
        <h1 class="mb-6 text-4xl font-bold tracking-tight text-white md:text-5xl lg:text-6xl">
          {{ sharedPlan?.title || '특별한 여행 계획이' }}
          <span class="text-purple-400">공유</span>
          되었습니다
        </h1>

        <!-- 설명 -->
        <p class="mx-auto mb-8 max-w-2xl text-lg leading-relaxed text-slate-300">
          {{
            sharedPlan?.description ||
            '여행지를 별로, 이동경로를 별자리로 표현한 아름다운 여행 계획을 만나보세요. 누군가의 소중한 추억과 계획이 별자리가 되어 당신에게 전해집니다.'
          }}
        </p>

        <!-- 여행 정보 -->
        <div v-if="sharedPlan" class="mb-8 flex flex-wrap justify-center gap-6 text-slate-400">
          <div class="flex items-center gap-2">
            <span>📅</span>
            <span>{{ Object.keys(sharedPlan.details).length }}일간의 여행</span>
          </div>
          <div class="flex items-center gap-2">
            <span>👤</span>
            <span>{{ sharedPlan.planWriters.map(w => w.name).join(', ') }} 님의 추천</span>
          </div>
          <div class="flex items-center gap-2">
            <span>❤️</span>
            <span>{{ sharedPlan.likeCount }}명이 좋아함</span>
          </div>
        </div>

        <!-- 태그 -->
        <div v-if="sharedPlan?.tags?.length" class="mb-12 flex flex-wrap justify-center gap-2">
          <Badge
            v-for="tag in sharedPlan.tags"
            :key="tag.tagId"
            variant="outline"
            class="border-purple-400/50 text-xs text-purple-300"
          >
            {{ tag.name }}
          </Badge>
        </div>

        <!-- 별자리 컴포넌트 -->
        <div ref="containerRef" class="relative flex items-center justify-center overflow-visible">
          <div
            v-if="sharedPlan?.stella"
            ref="constellationRef"
            :style="transformStyle"
            class="constellation-wrapper z-50"
            :class="constellationClasses"
          >
            <MainConstellationCard :stella="sharedPlan.stella" />
          </div>
          <!-- 로딩 중일 때 표시할 플레이스홀더 -->
          <div
            v-else-if="isLoading"
            class="flex h-64 w-64 items-center justify-center rounded-lg border-2 border-dashed border-gray-400"
          >
            <span class="text-gray-400">로딩 중...</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.constellation-wrapper {
  transform-origin: center;
  position: relative;
}

.with-transition {
  transition: all 2.5s cubic-bezier(0.22, 1, 0.36, 1);
}

/* 애니메이션 없이 즉시 중앙으로 이동 */
.center-position.no-transition {
  transform: translate(var(--translate-x), var(--translate-y)) scale(1.2);
  transition: none;
}

/* transition이 활성화된 후의 중앙 위치 */
.center-position.with-transition {
  transform: translate(var(--translate-x), var(--translate-y)) scale(1.2);
}

/* 원래 위치로 돌아갈 때 */
.with-transition:not(.center-position) {
  transform: translate(0, 0) scale(1);
}

.overflow-visible {
  overflow: visible;
}

@media (max-width: 768px) {
  .flex {
    flex-direction: column;
  }

  .flex-1 {
    flex: none;
    width: 100%;
  }
}
</style>
