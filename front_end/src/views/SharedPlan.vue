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
import { toPng, toJpeg } from 'html-to-image';

const route = useRoute();
const router = useRouter();

// 상태 관리
const isLoading = ref(true);
const hasError = ref(false);
const sharedPlan = ref<PlanDetail | null>(null);
const isDownloading = ref(false);

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

/**
 * 별자리 카드를 이미지로 다운로드
 */
const downloadConstellationImage = async (format: 'png' | 'jpeg' = 'png'): Promise<void> => {
  if (!constellationRef.value || !sharedPlan.value?.stella) {
    toast.error('다운로드할 별자리 카드가 없습니다');
    return;
  }

  try {
    isDownloading.value = true;

    // 다운로드 옵션 설정
    const options = {
      quality: 1, // 최고 품질
      pixelRatio: 2, // 고해상도
      backgroundColor: 'transparent', // 투명 배경
      style: {
        transform: 'scale(1)', // 변환 효과 제거
        transformOrigin: 'center',
      },
      // 폰트 로딩 대기
      skipFonts: false,
      // 이미지 로딩 대기
      useCORS: true,
    };

    let dataUrl: string;
    const fileName = `constellation-${stellaAI.value?.cardName || 'card'}-${Date.now()}`;

    // 포맷에 따라 다른 함수 사용
    if (format === 'jpeg') {
      dataUrl = await toJpeg(constellationRef.value, {
        ...options,
        backgroundColor: '#1a1a2e', // JPEG는 투명 배경 지원 안 함
      });
    } else {
      dataUrl = await toPng(constellationRef.value, options);
    }

    // 다운로드 실행
    const link = document.createElement('a');
    link.download = `${fileName}.${format}`;
    link.href = dataUrl;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    toast.success('별자리 카드가 다운로드되었습니다! ✨', {
      description: `${fileName}.${format} 파일로 저장되었습니다.`,
      duration: 3000,
    });
  } catch (error) {
    console.error('이미지 다운로드 실패:', error);
    toast.error('이미지 다운로드에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isDownloading.value = false;
  }
};

/**
 * 고화질 별자리 카드 다운로드 (PNG)
 */
const downloadHighQualityImage = (): Promise<void> => {
  return downloadConstellationImage('png');
};

/**
 * 일반 별자리 카드 다운로드 (JPEG)
 */
const downloadStandardImage = (): Promise<void> => {
  return downloadConstellationImage('jpeg');
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

            <!-- 다운로드 버튼 추가 -->
            <div
              v-if="sharedPlan?.stella && !showOverlay"
              class="flex flex-wrap justify-center gap-3 lg:justify-start"
            >
              <button
                @click="downloadHighQualityImage"
                :disabled="isDownloading"
                class="flex items-center gap-2 rounded-full border border-purple-400/50 bg-purple-500/20 px-6 py-3 text-sm font-medium text-purple-300 transition-all hover:scale-105 hover:bg-purple-500/30 disabled:cursor-not-allowed disabled:opacity-50"
              >
                <svg
                  v-if="!isDownloading"
                  class="h-4 w-4"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H4a2 2 0 01-2-2V7a2 2 0 012-2h5l2 2h5a2 2 0 012 2z"
                  ></path>
                </svg>
                <svg v-else class="h-4 w-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle
                    class="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    stroke-width="4"
                  ></circle>
                  <path
                    class="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
                {{ isDownloading ? '다운로드 중...' : '고화질 PNG' }}
              </button>

              <button
                @click="downloadStandardImage"
                :disabled="isDownloading"
                class="flex items-center gap-2 rounded-full border border-pink-400/50 bg-pink-500/20 px-6 py-3 text-sm font-medium text-pink-300 transition-all hover:scale-105 hover:bg-pink-500/30 disabled:cursor-not-allowed disabled:opacity-50"
              >
                <svg
                  v-if="!isDownloading"
                  class="h-4 w-4"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"
                  ></path>
                </svg>
                <svg v-else class="h-4 w-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle
                    class="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    stroke-width="4"
                  ></circle>
                  <path
                    class="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
                {{ isDownloading ? '압축 중...' : 'JPEG 저장' }}
              </button>
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
