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
import type { StellaAI } from '@/services/api/domains/stella/types';
import { ROUTES } from '@/router/routes';

const route = useRoute();
const router = useRouter();
// const router = useRouter();

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

const stellaAI = ref<StellaAI | null>(null);

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
    stellaAI.value = response.stellaAI;

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

    <!-- 히어로 섹션 -->
    <section
      class="relative flex min-h-screen items-center justify-center px-4 py-10 sm:px-6 lg:px-8"
    >
      <!-- 배경 오버레이 -->
      <div
        class="bg-opacity-90 fixed inset-0 z-50 bg-black transition-opacity duration-1000"
        :class="{ 'opacity-100': showOverlay, 'pointer-events-none opacity-0': !showOverlay }"
      ></div>

      <div class="relative mx-auto max-w-6xl">
        <div class="grid grid-cols-1 items-center gap-12 lg:grid-cols-2">
          <!-- 왼쪽: 텍스트 콘텐츠 -->
          <div class="space-y-8 text-center lg:text-left">
            <!-- AI 카드 이름 -->
            <div v-if="stellaAI" class="inline-block">
              <span
                class="rounded-full border border-purple-400/30 bg-gradient-to-r from-purple-500/20 to-pink-500/20 px-4 py-2 text-sm font-medium text-purple-300"
              >
                {{ stellaAI.cardName }}
              </span>
            </div>

            <!-- 메인 제목 -->
            <h1
              class="text-4xl leading-tight font-bold tracking-tight text-white md:text-5xl lg:text-6xl"
            >
              별자리가 전하는
              <span
                class="bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent"
              >
                여행의 메시지
              </span>
            </h1>

            <!-- AI 메시지 -->
            <div v-if="stellaAI" class="relative">
              <div class="absolute -top-2 -left-2 font-serif text-6xl text-purple-400/60">"</div>
              <p class="pl-8 text-lg leading-relaxed font-light text-slate-300 italic md:text-xl">
                {{ stellaAI.message }}
              </p>
              <div
                class="absolute -right-2 -bottom-4 rotate-180 font-serif text-6xl text-purple-400/60"
              >
                "
              </div>
            </div>

            <!-- 키워드 태그 -->
            <div
              v-if="stellaAI?.keywords?.length"
              class="flex flex-wrap justify-center gap-3 lg:justify-start"
            >
              <Badge
                v-for="keyword in stellaAI.keywords"
                :key="keyword"
                variant="outline"
                class="border-purple-400/50 bg-purple-500/10 px-4 py-2 text-purple-300 transition-colors hover:bg-purple-500/20"
              >
                ✨ {{ keyword }}
              </Badge>
            </div>
          </div>

          <!-- 오른쪽: 별자리 카드 -->
          <div ref="containerRef" class="relative flex items-center justify-center">
            <div
              v-if="sharedPlan?.stella"
              ref="constellationRef"
              :style="transformStyle"
              class="constellation-wrapper z-50"
              :class="constellationClasses"
            >
              <MainConstellationCard :stella="sharedPlan.stella" :subtitle="stellaAI?.cardName" />
            </div>
            <!-- 로딩 중일 때 표시할 플레이스홀더 -->
            <div
              v-else
              class="flex h-96 w-96 items-center justify-center rounded-2xl border border-purple-400/20 bg-gradient-to-br from-purple-900/20 to-pink-900/20"
            >
              <div class="animate-pulse text-purple-400">
                <svg class="h-16 w-16" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.196-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"
                  ></path>
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 하단 CTA 섹션 -->
    <section class="relative px-4 py-20 sm:px-6 lg:px-8">
      <div class="mx-auto max-w-4xl text-center">
        <div
          class="rounded-3xl border border-purple-400/30 bg-gradient-to-r from-purple-600/20 to-pink-600/20 p-12 backdrop-blur-lg"
        >
          <h3 class="mb-6 text-3xl font-bold text-white">당신만의 별자리 여행을 시작하세요</h3>
          <p class="mx-auto mb-8 max-w-2xl text-lg text-slate-300">
            AI가 분석한 당신의 여행 성향으로 특별한 별자리 여행 계획을 만들어보세요
          </p>
          <button
            class="transform rounded-full bg-gradient-to-r from-purple-500 to-pink-500 px-8 py-4 font-semibold text-white shadow-lg transition-all duration-300 hover:scale-105 hover:from-purple-600 hover:to-pink-600 hover:shadow-xl"
            @click="router.push(ROUTES.MAIN.path)"
          >
            나만의 별자리 만들기 ✨
          </button>
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

/* 그라데이션 텍스트 애니메이션 */
@keyframes gradient-shift {
  0%,
  100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.bg-gradient-to-r {
  background-size: 200% 200%;
  animation: gradient-shift 6s ease infinite;
}

/* 반응형 개선 */
@media (max-width: 1024px) {
  .grid-cols-1.lg\\:grid-cols-2 {
    gap: 2rem;
  }

  .lg\\:text-left {
    text-align: center;
  }

  .lg\\:justify-start {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .text-4xl.md\\:text-5xl.lg\\:text-6xl {
    font-size: 2.5rem;
    line-height: 1.2;
  }

  .text-lg.md\\:text-xl {
    font-size: 1.1rem;
  }

  .grid-cols-1.md\\:grid-cols-3 {
    gap: 1rem;
  }
}
</style>
