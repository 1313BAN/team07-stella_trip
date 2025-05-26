<script setup lang="ts">
import MainConstellationCard from '@/components/MainConstellationCard/MainConstellationCard.vue';
import { onMounted } from 'vue';
import { useAnimationStore } from '@/stores/animation';
import { useConstellationAnimation } from '@/composables/useConstellationAnimation';

// Pinia 스토어 사용
const animationStore = useAnimationStore();

// 별자리 애니메이션 컴포저블 사용
const {
  showOverlay,
  transformStyle,
  constellationRef,
  containerRef,
  startAnimation,
  constellationClasses,
} = useConstellationAnimation({
  duration: 4000,
  loadDelay: 50,
  transitionDelay: 100,
  onComplete: () => {
    // 애니메이션 완료 후 상태를 "본 적 있음"으로 설정
    animationStore.setIntroAnimationSeen();
  },
});

onMounted(() => {
  if (!animationStore.hasSeenIntroAnimation) {
    // 처음 보는 사용자라면 애니메이션 실행
    startAnimation();
  }
});
</script>

<template>
  <section class="relative flex min-h-[calc(100vh-5rem)] items-center px-4 py-10 sm:px-6 lg:px-8">
    <!-- 배경 오버레이 -->
    <div
      class="bg-opacity-90 fixed inset-0 z-50 bg-black transition-opacity duration-1000"
      :class="{ 'opacity-100': showOverlay, 'pointer-events-none opacity-0': !showOverlay }"
    ></div>

    <div class="relative container mx-auto">
      <div class="flex items-center gap-10">
        <!-- 왼쪽: 텍스트 및 버튼 -->
        <div class="flex-1 flex-col items-start text-left">
          <!-- 메인 타이틀 -->
          <h1 class="mb-6 text-4xl font-bold tracking-tight text-white md:text-5xl lg:text-6xl">
            당신의 여행을
            <span class="text-purple-400">별자리</span>
            로 만들어 보세요
          </h1>

          <!-- 서브타이틀 -->
          <p class="mb-8 max-w-lg text-lg leading-relaxed text-slate-300">
            여행지를 별로, 이동경로를 별자리로 표현하여 나만의 특별한 여행 계획을 만들고
            공유해보세요. 우주처럼 무한한 가능성의 여행을 별자리와 함께 디자인해보세요.
          </p>

          <!-- 버튼 그룹 -->
          <div class="flex flex-wrap gap-4">
            <button
              class="rounded-full bg-purple-500 px-6 py-3 font-medium text-white transition-all hover:bg-purple-600"
            >
              여행 계획 시작하기
            </button>
            <button
              class="rounded-full border border-purple-400 bg-transparent px-6 py-3 font-medium text-white transition-all hover:bg-purple-500/10"
            >
              별자리 불러오기
            </button>
          </div>
        </div>

        <!-- 오른쪽: 별자리 컴포넌트 -->
        <div
          ref="containerRef"
          class="relative flex flex-1 items-center justify-center overflow-visible"
        >
          <div
            ref="constellationRef"
            :style="transformStyle"
            class="constellation-wrapper z-50 w-full"
            :class="constellationClasses"
          >
            <MainConstellationCard />
          </div>
        </div>
      </div>
    </div>
  </section>
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
