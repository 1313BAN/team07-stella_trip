<script setup lang="ts">
import Constellation from '@/components/MainConstellationCard/MainConstellationCard.vue';
import { ref, onMounted, reactive, watch, onUnmounted } from 'vue';
import { useAnimationStore } from '@/stores/animation'; // 스토어 import

// Pinia 스토어 사용
const animationStore = useAnimationStore();

// 애니메이션 상태
const isCenter = ref(false);
const showOverlay = ref(false); // 오버레이 상태를 별도로 관리
const constellationRef = ref<HTMLDivElement | null>(null);
const containerRef = ref<HTMLDivElement | null>(null);
const isPositioned = ref(false);
const transformStyle = reactive({
  '--translate-x': '0px',
  '--translate-y': '0px',
});

// showOverlay 상태가 변경될 때마다 body의 overflow 속성 조절
watch(showOverlay, newVal => {
  if (newVal) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = '';
  }
});

// 애니메이션 실행 함수
const startIntroAnimation = () => {
  // 처음부터 어두운 배경과 스크롤 막기
  showOverlay.value = true;
  document.body.style.overflow = 'hidden';

  // DOM이 완전히 로드된 후 위치 계산
  setTimeout(() => {
    if (constellationRef.value && containerRef.value) {
      // 컴포넌트의 현재 위치 계산
      const rect = constellationRef.value.getBoundingClientRect();

      // 화면 중앙 계산
      const centerX = window.innerWidth / 2;
      const centerY = window.innerHeight / 2;

      // 컴포넌트 중심점 계산
      const componentCenterX = rect.left + rect.width / 2;
      const componentCenterY = rect.top + rect.height / 2;

      // 중앙으로 이동하기 위한 transform 오프셋 계산
      const deltaX = centerX - componentCenterX;
      const deltaY = centerY - componentCenterY;

      // CSS 변수에 적용
      transformStyle['--translate-x'] = `${deltaX}px`;
      transformStyle['--translate-y'] = `${deltaY}px`;

      // 즉시 중앙 위치로 설정 (애니메이션 없이)
      isCenter.value = true;

      // 잠깐 후 transition 활성화
      setTimeout(() => {
        isPositioned.value = true;
      }, 100);
    }
  }, 50);

  // 4초 후 원래 위치로 애니메이션 (이때부터 transition 적용)
  setTimeout(() => {
    isCenter.value = false;
    showOverlay.value = false; // 배경도 함께 사라짐
    // 애니메이션 완료 후 상태를 "본 적 있음"으로 설정
    setTimeout(() => {
      animationStore.setIntroAnimationSeen();
    }, 2500); // transition 완료 후
  }, 4000);
};

onMounted(() => {
  // 처음 보는 사용자인지 확인
  if (!animationStore.hasSeenIntroAnimation) {
    // 처음 보는 사용자라면 애니메이션 실행
    startIntroAnimation();
  } else {
    // 이미 본 사용자라면 애니메이션 없이 바로 일반 상태
    document.body.style.overflow = '';
  }
});

// 컴포넌트가 언마운트될 때 스크롤 제한 해제
onUnmounted(() => {
  document.body.style.overflow = '';
});

// 개발용: 애니메이션 리셋 함수 (필요시 사용)
const resetAnimation = () => {
  animationStore.resetIntroAnimation();
  location.reload(); // 페이지 새로고침으로 애니메이션 다시 확인
};
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

            <!-- 개발용 버튼 (프로덕션에서는 제거) -->
            <!-- <button
              @click="resetAnimation"
              class="rounded-full border border-red-400 bg-transparent px-4 py-2 text-sm font-medium text-red-400 transition-all hover:bg-red-500/10"
            >
              애니메이션 리셋
            </button> -->
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
            :class="{
              'center-position': isCenter,
              'with-transition': isPositioned,
              'no-transition': isCenter && !isPositioned,
            }"
          >
            <Constellation />
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
