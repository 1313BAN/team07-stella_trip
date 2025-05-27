<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue';
import ConstellationComponent from '@/components/MainConstellationCard/ConstellationComponent.vue';
import type { StellaData } from '@/services/api/domains/plan/types';

// props 정의 (stella, title, subtitle 추가)
interface Props {
  stella?: StellaData;
  title?: string;
  subtitle?: string;
  onComplete?: () => void;
}

const props = withDefaults(defineProps<Props>(), {
  title: 'Stella Trip',
  subtitle: 'Hunter',
  onComplete: () => {},
});

// 모든 요소가 즉시 표시되도록 초기값을 1로 설정
const nameOpacity = ref(1);
const bgOpacity = ref(1);

// 테두리도 즉시 100%로 설정
const topBorderWidth = ref(100);
const rightBorderHeight = ref(100);
const bottomBorderWidth = ref(100);
const leftBorderHeight = ref(100);

// 파티클 생성 로직 - 기존과 동일
const particles = computed(() => {
  return Array(20)
    .fill(null)
    .map((_, i) => ({
      id: `particle-${i}`,
      class: getParticleClass(i),
      top: `${10 + Math.random() * 80}%`,
      left: `${10 + Math.random() * 80}%`,
      animationDelay: `${Math.random() * 10}s`,
      animationDuration: `${14 + Math.random() * 6}s`,
    }));
});

// 드림 파티클 생성 로직 - 기존과 동일
const dreamParticles = computed(() => {
  return Array(5)
    .fill(null)
    .map((_, i) => ({
      id: `dream-${i}`,
      top: `${10 + Math.random() * 80}%`,
      left: `${10 + Math.random() * 80}%`,
      animationDelay: `${Math.random() * 12}s`,
    }));
});

// 파티클 클래스 생성 함수 - 기존과 동일
const getParticleClass = (index: number): string => {
  const classes = ['particle-tiny', 'particle-small', 'particle-medium', 'particle-large'];
  return classes[index % classes.length];
};

// 틸트 효과를 위한 변수 - 기존과 동일 (호버 효과 유지)
const cardRef = ref<HTMLElement | null>(null);
const tilt = reactive({
  x: 0,
  y: 0,
  active: false,
});

// 마우스 이벤트 핸들러 (틸트 효과) - 기존과 동일
const handleMouseMove = (event: MouseEvent) => {
  if (!cardRef.value) return;

  const rect = cardRef.value.getBoundingClientRect();
  const centerX = rect.left + rect.width / 2;
  const centerY = rect.top + rect.height / 2;

  // 마우스 위치를 중앙 기준으로 계산 (-1 ~ 1 범위)
  const mouseX = (event.clientX - centerX) / (rect.width / 2);
  const mouseY = (event.clientY - centerY) / (rect.height / 2);

  // 틸트 각도 계산 (최대 ±5도)
  tilt.x = mouseY * -5; // Y축 기준 회전 (마우스 Y 이동에 따라 X축으로 기울임)
  tilt.y = mouseX * 5; // X축 기준 회전 (마우스 X 이동에 따라 Y축으로 기울임)
  tilt.active = true;
};

const handleMouseLeave = () => {
  // 마우스가 카드를 벗어나면 기울기를 원래대로 복원
  tilt.x = 0;
  tilt.y = 0;
  tilt.active = false;
};

onMounted(() => {
  // 애니메이션 시작 대신 완료 콜백만 호출
  if (props.onComplete) {
    props.onComplete();
  }
});
</script>

<template>
  <div class="flex items-center justify-center">
    <!-- 카드 컨테이너 -->
    <div
      ref="cardRef"
      class="card-container relative aspect-[3/5] w-80 overflow-hidden rounded-xl"
      :style="{
        transform: tilt.active
          ? `perspective(1000px) rotateX(${tilt.x}deg) rotateY(${tilt.y}deg)`
          : 'perspective(1000px) rotateX(0deg) rotateY(0deg)',
        transition: tilt.active ? 'transform 0.1s ease-out' : 'transform 0.5s ease-out',
      }"
      @mousemove="handleMouseMove"
      @mouseleave="handleMouseLeave"
    >
      <!-- 카드 구조 -->
      <div class="tarot-card relative h-full w-full overflow-hidden">
        <!-- 1. 검정 배경 -->
        <div
          class="black-bg absolute inset-0 transition-opacity duration-800"
          :style="{
            background:
              'linear-gradient(180deg, #0a0a1a 0%, #1a1a2e 30%, #0f0f23 70%, #000000 100%)',
            opacity: bgOpacity,
            borderRadius: '15px',
          }"
        ></div>

        <!-- 2. 그라데이션 배경 -->
        <div
          class="gradient-bg absolute inset-0 transition-opacity duration-800"
          :style="{
            background: `
              linear-gradient(135deg, 
                rgba(79, 70, 229, ${bgOpacity * 0.1}) 0%,
                rgba(139, 92, 246, ${bgOpacity * 0.08}) 20%,
                rgba(219, 39, 119, ${bgOpacity * 0.06}) 40%,
                rgba(59, 130, 246, ${bgOpacity * 0.05}) 60%,
                rgba(139, 92, 246, ${bgOpacity * 0.08}) 80%,
                rgba(79, 70, 229, ${bgOpacity * 0.1}) 100%
              ),
              radial-gradient(ellipse at 30% 20%, rgba(79, 70, 229, ${bgOpacity * 0.08}) 0%, transparent 60%),
              radial-gradient(ellipse at 70% 80%, rgba(139, 92, 246, ${bgOpacity * 0.06}) 0%, transparent 50%)
            `,
            opacity: bgOpacity * 0.9,
            boxShadow: `
              0 0 40px rgba(79, 70, 229, ${bgOpacity * 0.15}),
              inset 0 0 60px rgba(139, 92, 246, ${bgOpacity * 0.03})
            `,
          }"
        ></div>

        <!-- 3. 카드 테두리 - 완전히 표시된 상태 -->
        <div class="card-border-container absolute top-5 right-5 bottom-5 left-5 z-10">
          <!-- 상단 테두리 -->
          <div
            class="border-top absolute top-0 left-0 h-0.5 duration-300 ease-out"
            :style="{
              width: `${topBorderWidth}%`,
              background: `linear-gradient(90deg, rgba(139, 92, 246, 0.2), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>

          <!-- 우측 테두리 -->
          <div
            class="border-right absolute top-0 right-0 w-0.5 duration-300 ease-out"
            :style="{
              height: `${rightBorderHeight}%`,
              background: `linear-gradient(180deg, rgba(139, 92, 246, 0.5), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>

          <!-- 하단 테두리 -->
          <div
            class="border-bottom absolute right-0 bottom-0 h-0.5 duration-300 ease-out"
            :style="{
              width: `${bottomBorderWidth}%`,
              background: `linear-gradient(270deg, rgba(139, 92, 246, 0.5), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>

          <!-- 좌측 테두리 -->
          <div
            class="border-left absolute bottom-0 left-0 w-0.5 duration-300 ease-out"
            :style="{
              height: `${leftBorderHeight}%`,
              background: `linear-gradient(0deg, rgba(139, 92, 246, 0.5), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>
        </div>

        <!-- 4. 은하수 배경 -->
        <div
          class="milky-way absolute transition-opacity duration-700"
          :style="{
            top: '-50%',
            left: '-50%',
            width: '200%',
            height: '200%',
            borderRadius: '40%',
            opacity: bgOpacity * 0.9,
          }"
        ></div>

        <!-- 오라 효과 -->
        <div
          class="aura absolute transition-opacity duration-700"
          :style="{
            top: '50%',
            left: '50%',
            width: '330px',
            height: '330px',
            borderRadius: '50%',
            transform: 'translate(-50%, -50%)',
            opacity: bgOpacity * 0.8,
          }"
        ></div>

        <!-- 별자리 콘텐츠 -->
        <ConstellationComponent :stella="stella" />

        <!-- 파티클들 - 동적 위치 적용 -->
        <div
          v-for="particle in particles"
          :key="particle.id"
          :class="particle.class"
          class="particle pointer-events-none absolute rounded-full transition-opacity duration-700"
          :style="{
            opacity: bgOpacity * 0.8,
            top: particle.top,
            left: particle.left,
            animationDelay: particle.animationDelay,
            animationDuration: particle.animationDuration,
            zIndex: 15,
          }"
        ></div>

        <!-- 드림 파티클 - 동적 위치 적용 -->
        <div
          v-for="particle in dreamParticles"
          :key="particle.id"
          class="dream-particle pointer-events-none absolute rounded-full transition-opacity duration-700"
          :style="{
            opacity: bgOpacity * 0.6,
            top: particle.top,
            left: particle.left,
            animationDelay: particle.animationDelay,
            zIndex: 15,
          }"
        ></div>

        <!-- 유성 효과 -->
        <div
          class="shooting-star-1 pointer-events-none absolute h-0.5 w-24 transition-opacity duration-700"
          :style="{ opacity: bgOpacity * 0.9 }"
        ></div>
        <div
          class="shooting-star-2 pointer-events-none absolute h-0.5 w-16 transition-opacity duration-700"
          :style="{ opacity: bgOpacity * 0.7 }"
        ></div>

        <!-- 5. 카드 타이틀 - 즉시 표시 -->
        <div
          class="absolute top-8 left-1/2 -translate-x-1/2 transform font-serif text-xl font-semibold tracking-wider text-nowrap uppercase transition-opacity duration-700"
          :style="{
            opacity: nameOpacity,
            letterSpacing: '6px',
            textShadow:
              '0 0 20px rgba(255, 255, 255, 0.9), 0 0 40px rgba(180, 160, 250, 0.8), 0 2px 4px rgba(0, 0, 0, 0.8)',
            color: 'rgba(240, 235, 255, 0.95)',
            fontFamily: '\'Cinzel\', serif',
            zIndex: 20,
          }"
        >
          {{ title }}
        </div>

        <!-- 카드 하단 텍스트 - 즉시 표시 -->
        <div
          class="absolute bottom-8 left-1/2 -translate-x-1/2 transform text-center font-serif text-sm font-medium tracking-wider uppercase transition-opacity duration-700"
          :style="{
            opacity: nameOpacity,
            letterSpacing: '3px',
            textShadow:
              '0 0 15px rgba(255, 255, 255, 0.8), 0 0 30px rgba(180, 160, 250, 0.6), 0 2px 4px rgba(0, 0, 0, 0.8)',
            color: 'rgba(240, 235, 255, 0.9)',
            fontFamily: '\'Cinzel\', serif',
            zIndex: 20,
          }"
        >
          <div class="flex items-center justify-center">
            <span class="mr-2">✦</span>
            <span class="text-center whitespace-pre-line">{{ subtitle }}</span>
            <span class="ml-2">✦</span>
          </div>
        </div>

        <!-- 카드 필터 효과 -->
        <div
          class="card-filter pointer-events-none absolute inset-0 transition-opacity duration-700"
          :style="{ opacity: bgOpacity }"
        ></div>
      </div>
    </div>
  </div>
</template>
