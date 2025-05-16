<script setup lang="ts">
import useInView from '@/composables/useInView';
import { useStaggeredAnimation } from '@/composables/useStaggeredAnimation';
import { ref } from 'vue';

const containerRef = ref<Element | null>(null);
const { isInView: textContainerInView } = useInView(containerRef, {
  threshold: 0.3,
  triggerOnce: true,
});

const { getItemClass: getTextItemClass, getItemStyle: getTextItemStyle } = useStaggeredAnimation({
  trigger: textContainerInView,
  fromClass: 'opacity-0 translate-y-8',
  toClass: 'opacity-100 translate-y-0',
  baseClass: 'text-lg leading-relaxed text-slate-300 lg:text-xl',
  staggerDelay: 150,
  duration: 700,
  easing: 'ease-out',
});

const constellationRef = ref<Element | null>(null);
const { isInView: constellationInView } = useInView(constellationRef, {
  threshold: 0.4,
  triggerOnce: true,
});

// 별자리 애니메이션
const { getItemClass: getConstellationItemClass, getItemStyle: getConstellationItemStyle } =
  useStaggeredAnimation({
    trigger: constellationInView,
    fromClass: 'opacity-0 scale-75 blur-md translate-y-12',
    toClass: 'opacity-100 scale-100 blur-0 translate-y-0',
    baseClass: 'absolute transform-gpu',
    staggerDelay: 100,
    duration: 1000,
    easing: 'ease-out',
  });

// 별자리 이미지 데이터
const constellations = [
  {
    id: 1,
    rotation: -15,
    scale: 0.8,
    opacity: 0.3,
    zIndex: 1,
    position: { x: -20, y: 10 },
  },
  {
    id: 2,
    rotation: 20,
    scale: 0.9,
    opacity: 0.4,
    zIndex: 2,
    position: { x: 25, y: -15 },
  },
  {
    id: 3,
    rotation: 0,
    scale: 1,
    opacity: 1,
    zIndex: 3,
    position: { x: 0, y: 0 },
    isMain: true,
  },
  {
    id: 4,
    rotation: -25,
    scale: 0.85,
    opacity: 0.35,
    zIndex: 1,
    position: { x: -30, y: -20 },
  },
  {
    id: 5,
    rotation: 10,
    scale: 0.75,
    opacity: 0.25,
    zIndex: 0,
    position: { x: 35, y: 25 },
  },
];

//TODO: 버튼 컴포넌트화, 임시 별자리 변경
</script>

<template>
  <section class="relative min-h-screen overflow-hidden bg-slate-950/50">
    <div class="container mx-auto px-6 py-20 lg:py-32">
      <div class="grid min-h-[80vh] items-center gap-16 lg:grid-cols-2 lg:gap-24">
        <!-- 텍스트 컨텐츠 -->
        <div ref="containerRef" class="space-y-8 lg:space-y-12">
          <!-- 제목 (인덱스 0) - baseClass 사용 안 함 -->
          <h2
            :class="getTextItemClass(false)"
            :style="getTextItemStyle(0)"
            class="text-4xl leading-tight font-bold lg:text-6xl"
          >
            <span class="inline-block text-gray-200">나만의 여행을 별자리로 만들어보세요</span>
          </h2>

          <!-- 첫 번째 문단 (인덱스 1) - baseClass 적용으로 class 불필요 -->
          <p :class="getTextItemClass()" :style="getTextItemStyle(1)">
            방문했던 곳들, 만난 사람들을 별처럼 연결해 보세요. 각각의 여행 순간들이 하나의 이야기가
            되어
            <span class="font-medium text-purple-300">나만의 별자리</span>
            로 기록됩니다.
          </p>

          <!-- 두 번째 문단 (인덱스 2) - baseClass 적용으로 class 불필요 -->
          <p :class="getTextItemClass()" :style="getTextItemStyle(2)">
            시간이 지날수록 더욱 풍부해지는 여행의 역사를 한눈에 확인하고, 소중한 추억들을 영원히
            간직하세요.
          </p>

          <!-- 세 번째 문단 (인덱스 3) - baseClass 적용으로 class 불필요 -->
          <p :class="getTextItemClass()" :style="getTextItemStyle(3)">
            별자리가 되는 입자인 별처럼, 모든 장소와 짧다면 짧고 길다면 길은 여정
            <span class="font-medium text-blue-300">각각의 의미</span>
            를 담아 기록해보세요.
          </p>

          <!-- 네 번째 문단 (인덱스 4) - baseClass 적용으로 class 불필요 -->
          <p :class="getTextItemClass()" :style="getTextItemStyle(4)">
            당신의 여행은 더 이상 끝나지 않습니다. 매 순간이 새로운 별이 되어 끊임없이 확장하는
            우주가 될 것입니다.
          </p>

          <!-- CTA 버튼 (인덱스 5) - baseClass 사용 안 함 -->
          <div class="pt-8">
            <button
              :class="getTextItemClass(false)"
              :style="getTextItemStyle(5)"
              class="group inline-flex items-center gap-3 rounded-full bg-gradient-to-r from-purple-600 to-blue-600 px-8 py-4 text-lg font-semibold text-white transition-all duration-300 hover:scale-105 hover:from-purple-500 hover:to-blue-500 hover:shadow-xl hover:shadow-purple-500/20 focus:ring-4 focus:ring-purple-500/20 focus:outline-none active:scale-95 disabled:cursor-not-allowed disabled:opacity-50"
            >
              <span>별자리 만들기</span>
              <svg
                class="h-5 w-5 transition-transform group-hover:translate-x-1"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M9 5l7 7-7 7"
                />
              </svg>
            </button>
          </div>
        </div>

        <!-- 별자리 이미지 영역 -->
        <div ref="constellationRef" class="relative h-[600px] lg:h-[700px]">
          <div class="absolute inset-0 flex items-center justify-center">
            <!-- 겹쳐진 별자리 이미지들 -->
            <div
              v-for="(constellation, index) in constellations"
              :key="constellation.id"
              :class="getConstellationItemClass()"
              :style="{
                ...getConstellationItemStyle(index),
                transform: constellationInView
                  ? `translate(${constellation.position.x}px, ${constellation.position.y}px) 
                     rotate(${constellation.rotation}deg) 
                     scale(${constellation.scale})
                     ${constellation.isMain ? 'translateZ(20px)' : ''}`
                  : undefined,
                opacity: constellationInView ? constellation.opacity : undefined,
                zIndex: constellation.zIndex,
                filter: constellation.isMain
                  ? 'brightness(1.2) contrast(1.1)'
                  : 'brightness(0.8) contrast(0.9)',
              }"
            >
              <!-- 임시 SVG 별자리 -->
              <svg width="300" height="300" viewBox="0 0 300 300" class="text-purple-300">
                <!-- 별들 -->
                <circle cx="50" cy="50" r="3" fill="currentColor" class="animate-pulse" />
                <circle
                  cx="150"
                  cy="30"
                  r="4"
                  fill="currentColor"
                  class="animate-pulse"
                  style="animation-delay: 0.5s"
                />
                <circle
                  cx="250"
                  cy="70"
                  r="3"
                  fill="currentColor"
                  class="animate-pulse"
                  style="animation-delay: 1s"
                />
                <circle
                  cx="80"
                  cy="150"
                  r="5"
                  fill="currentColor"
                  class="animate-pulse"
                  style="animation-delay: 1.5s"
                />
                <circle
                  cx="200"
                  cy="180"
                  r="3"
                  fill="currentColor"
                  class="animate-pulse"
                  style="animation-delay: 2s"
                />
                <circle
                  cx="120"
                  cy="250"
                  r="4"
                  fill="currentColor"
                  class="animate-pulse"
                  style="animation-delay: 2.5s"
                />

                <!-- 연결선 -->
                <line
                  x1="50"
                  y1="50"
                  x2="150"
                  y2="30"
                  stroke="currentColor"
                  stroke-width="1"
                  opacity="0.6"
                />
                <line
                  x1="150"
                  y1="30"
                  x2="250"
                  y2="70"
                  stroke="currentColor"
                  stroke-width="1"
                  opacity="0.6"
                />
                <line
                  x1="250"
                  y1="70"
                  x2="200"
                  y2="180"
                  stroke="currentColor"
                  stroke-width="1"
                  opacity="0.6"
                />
                <line
                  x1="80"
                  y1="150"
                  x2="200"
                  y2="180"
                  stroke="currentColor"
                  stroke-width="1"
                  opacity="0.6"
                />
                <line
                  x1="80"
                  y1="150"
                  x2="120"
                  y2="250"
                  stroke="currentColor"
                  stroke-width="1"
                  opacity="0.6"
                />
                <line
                  x1="50"
                  y1="50"
                  x2="80"
                  y2="150"
                  stroke="currentColor"
                  stroke-width="1"
                  opacity="0.6"
                />
              </svg>
            </div>

            <!-- 중앙 글로우 효과 -->
            <div
              :class="getConstellationItemClass(false)"
              :style="getConstellationItemStyle(constellations.length)"
              class="absolute inset-0 rounded-full"
              style="
                background: radial-gradient(circle, rgba(168, 85, 247, 0.3) 0%, transparent 70%);
              "
            ></div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
/* 3D 효과를 위한 추가 스타일 */
.transform-gpu {
  transform-style: preserve-3d;
}
</style>
