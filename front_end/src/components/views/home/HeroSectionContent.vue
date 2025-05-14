<template>
  <div
    class="relative z-10 flex min-h-screen flex-col items-center justify-center px-4 text-center"
  >
    <div
      class="max-w-5xl transition-all duration-1000"
      :class="props.isLoaded ? 'translate-y-0 opacity-100' : 'translate-y-8 opacity-0'"
    >
      <!-- 보조 텍스트 -->
      <div class="mb-6 text-sm font-medium tracking-widest text-purple-200/80 uppercase">
        Create Your Journey in Stars
      </div>

      <!-- 메인 타이틀 -->
      <h1
        class="mb-8 bg-gradient-to-r from-white via-purple-200 to-purple-300 bg-clip-text text-5xl leading-tight font-black tracking-tight text-transparent md:text-7xl"
      >
        <span class="block">Stella Trip</span>
        <span
          class="mt-2 block bg-gradient-to-r from-purple-400 via-pink-300 to-purple-300 bg-clip-text text-transparent"
        >
          별자리 여행 계획
        </span>
      </h1>

      <!-- 서브타이틀 -->
      <p
        class="mx-auto mb-12 max-w-4xl text-xl leading-relaxed font-light text-slate-300 md:text-2xl"
      >
        여행지를 별로, 이동경로를 별자리로 표현하는 특별한 여행 계획 서비스
        <br />
        실시간 채팅과 여행자 네트워킹으로 더 풍부한 여행 경험을 만들어보세요
      </p>

      <!-- 행동 버튼 -->
      <div class="mb-20 flex flex-col items-center justify-center gap-6 sm:flex-row">
        <button
          class="group relative overflow-hidden rounded-full bg-gradient-to-r from-purple-400 to-pink-400 px-8 py-4 text-lg font-semibold text-slate-900 transition-all duration-300 hover:scale-105 hover:shadow-2xl hover:shadow-purple-500/30"
          @click="onStartFree"
        >
          <span class="relative z-10">무료로 시작하기</span>
          <div
            class="absolute inset-0 bg-gradient-to-r from-purple-500 to-pink-500 opacity-0 transition-opacity duration-300 group-hover:opacity-100"
          ></div>
        </button>

        <button
          class="group relative rounded-full border-2 border-purple-400 px-8 py-4 text-lg font-semibold text-white transition-all duration-300 hover:scale-105 hover:bg-purple-500/20"
          @click="onLearnMore"
        >
          <span class="relative z-10">더 알아보기</span>
          <div
            class="absolute inset-0 rounded-full bg-purple-500/10 opacity-0 transition-opacity duration-300 group-hover:opacity-100"
          ></div>
        </button>
      </div>

      <!-- 특징 포인트 -->
      <div class="mb-16 flex flex-wrap justify-center gap-6 text-purple-200/80">
        <div class="flex items-center gap-2">
          <div class="h-2 w-2 animate-pulse rounded-full bg-purple-400"></div>
          <span class="text-sm font-medium">지도 위 별자리</span>
        </div>
        <div class="flex items-center gap-2">
          <div
            class="h-2 w-2 animate-pulse rounded-full bg-pink-400"
            style="animation-delay: 0.5s"
          ></div>
          <span class="text-sm font-medium">실시간 채팅</span>
        </div>
        <div class="flex items-center gap-2">
          <div
            class="h-2 w-2 animate-pulse rounded-full bg-purple-400"
            style="animation-delay: 1s"
          ></div>
          <span class="text-sm font-medium">여행자 네트워킹</span>
        </div>
      </div>
    </div>

    <!-- 스크롤 인디케이터 -->
    <ScrollIndicator @click="scrollToNext" />
  </div>
</template>

<script setup lang="ts">
import ScrollIndicator from '@/components/common/ScrollIndicator/ScrollIndicator.vue';

const props = defineProps<{
  isLoaded: boolean;
}>();

const emit = defineEmits<{
  startFree: [];
  learnMore: [];
  scrollToNext: [];
}>();

const onStartFree = () => {
  emit('startFree');
};

const onLearnMore = () => {
  emit('learnMore');
};

const scrollToNext = () => {
  emit('scrollToNext');
};
</script>
