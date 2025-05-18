<script setup lang="ts">
import Constellation from '@/components/MainConstellationCard/MainConstellationCard.vue';
import { ref, onMounted, reactive, watch, onUnmounted } from 'vue';

// 애니메이션 상태
const isCenter = ref(true);
const constellationRef = ref<HTMLDivElement | null>(null);
const containerRef = ref<HTMLDivElement | null>(null);
const isPositioned = ref(false); // 위치 설정 완료 상태
const transformStyle = reactive({
  '--translate-x': '0px',
  '--translate-y': '0px',
});

// isCenter 상태가 변경될 때마다 body의 overflow 속성 조절
watch(isCenter, newVal => {
  if (newVal) {
    // 중앙 위치일 때 스크롤 막기
    document.body.style.overflow = 'hidden';
  } else {
    // 중앙 위치가 아닐 때 스크롤 허용
    document.body.style.overflow = '';
  }
});

onMounted(() => {
  // 초기 상태에서도 스크롤 막기 적용
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

      // 위치 설정이 완료되었으므로 플래그 설정
      // 약간의 지연 후 설정하여 초기 이동이 보이지 않게 함
      setTimeout(() => {
        isPositioned.value = true;
      }, 50);
    }
  }, 100); // 약간의 지연으로 정확한 계산 보장

  // 5초 후 원래 위치로 애니메이션
  setTimeout(() => {
    isCenter.value = false;
  }, 4500);
});

// 컴포넌트가 언마운트될 때 스크롤 제한 해제 (안전장치)
onUnmounted(() => {
  document.body.style.overflow = '';
});
</script>

<template>
  <section class="relative flex min-h-[calc(100vh-5rem)] items-center px-4 py-10 sm:px-6 lg:px-8">
    <!-- 배경 오버레이 -->
    <div
      class="bg-opacity-90 fixed inset-0 z-10 bg-black transition-opacity duration-1000"
      :class="{ 'opacity-100': isCenter, 'pointer-events-none opacity-0': !isCenter }"
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
            class="constellation-wrapper z-10 w-full"
            :class="{
              'center-position': isCenter,
              'with-transition': isPositioned,
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
  /* 기본 상태에서는 transition 없음 */
}

/* isPositioned가 true일 때만 transition 적용 */
.with-transition {
  transition: all 2.5s cubic-bezier(0.22, 1, 0.36, 1);
}

.center-position {
  /* 계산된 transform 값을 변수로 사용 */
  transform: translate(var(--translate-x), var(--translate-y)) scale(1.2);
}

.overflow-visible {
  overflow: visible;
}

/* 반응형 조정 */
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
