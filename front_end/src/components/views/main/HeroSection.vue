<script setup lang="ts">
import { ref, onBeforeUnmount, watch } from 'vue';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  type CarouselApi,
} from '@/components/ui/carousel';
import StellaCard from '@/components/StellaCard/StellaCard.vue';

// 이벤트 발생을 위한 emit 정의
const emit = defineEmits<{
  createPlan: [];
  importConstellation: [];
}>();

// 이벤트 핸들러
const handleCreatePlan = () => {
  emit('createPlan');
};

const handleImportConstellation = () => {
  emit('importConstellation');
};

// 캐러셀 API와 현재 슬라이드 상태 관리
const carouselApi = ref<CarouselApi | null>(null);
const currentSlide = ref(0);
let autoRotateInterval: number | undefined;

// 페이드 효과를 위한 상태 관리
const isTransitioning = ref(false);

// API 설정 함수
function setApi(api: CarouselApi) {
  carouselApi.value = api;
}

// API 설정 이후 이벤트 처리
const apiWatcher = watch(
  carouselApi,
  api => {
    if (!api) return;

    // API 설정 후 이벤트 리스너 등록
    api.on('select', () => {
      currentSlide.value = api.selectedScrollSnap();
      isTransitioning.value = false;
    });

    api.on('settle', () => {
      isTransitioning.value = false;
    });

    // 자동 회전 시작
    startAutoRotate();
  },
  { immediate: true }
);

// 수동으로 슬라이드 변경 시 페이드 효과 적용
const goToSlide = (index: number) => {
  if (carouselApi.value) {
    isTransitioning.value = true;
    carouselApi.value.scrollTo(index);
  }
};

// 자동 회전 설정 - 페이드 효과 포함
const startAutoRotate = () => {
  // 기존 인터벌 제거
  if (autoRotateInterval) {
    clearInterval(autoRotateInterval);
  }

  // 새 인터벌 설정
  autoRotateInterval = window.setInterval(() => {
    if (carouselApi.value) {
      isTransitioning.value = true;
      const nextIndex = (currentSlide.value + 1) % 3;
      carouselApi.value.scrollTo(nextIndex);
    }
  }, 5000);
};

// 컴포넌트 언마운트 시 정리
onBeforeUnmount(() => {
  if (autoRotateInterval) {
    clearInterval(autoRotateInterval);
  }

  // watcher 정리
  apiWatcher();
});

// 캐러셀 옵션 설정 - 타입에 맞게 수정
const carouselOptions = {
  align: 'center' as const, // 'start' | 'center' | 'end'로 명시적 타입 지정
  loop: true,
  skipSnaps: false,
  inViewThreshold: 1,
  dragFree: false,
};
</script>

<template>
  <section
    class="relative z-10 flex min-h-[calc(100vh-5rem)] items-center px-4 py-10 sm:px-6 lg:px-8"
  >
    <div class="container mx-auto">
      <div class="grid grid-cols-1 items-center gap-10 lg:grid-cols-2">
        <!-- 왼쪽: 텍스트 및 버튼 -->
        <div class="flex flex-col items-start text-left">
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
              @click="handleCreatePlan"
            >
              여행 계획 시작하기
            </button>
            <button
              class="rounded-full border border-purple-400 bg-transparent px-6 py-3 font-medium text-white transition-all hover:bg-purple-500/10"
              @click="handleImportConstellation"
            >
              별자리 불러오기
            </button>
          </div>
        </div>

        <!-- 오른쪽: 캐러셀 -->
        <div class="mx-auto w-full overflow-visible">
          <div class="min-h-[520px]">
            <Carousel :opts="carouselOptions" class="h-auto w-full" @initApi="setApi">
              <CarouselContent class="min-h-[620px]">
                <CarouselItem
                  v-for="i in 3"
                  :key="i"
                  class="flex h-auto min-h-[600px] items-center justify-center transition-opacity duration-500"
                  :class="{
                    'opacity-100': currentSlide === i - 1,
                    'opacity-0': currentSlide !== i - 1,
                  }"
                >
                  <div class="flex w-full items-center justify-center py-2.5">
                    <StellaCard />
                  </div>
                </CarouselItem>
              </CarouselContent>
            </Carousel>
          </div>

          <!-- 슬라이드 인디케이터 -->
          <div class="mt-6 flex justify-center gap-2">
            <button
              v-for="i in 3"
              :key="i - 1"
              class="h-2 w-2 rounded-full transition-all"
              :class="i - 1 === currentSlide ? 'bg-white' : 'bg-white/50'"
              @click="goToSlide(i - 1)"
            ></button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
