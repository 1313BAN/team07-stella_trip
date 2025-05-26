<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue';
import ConstellationComponent from './ConstellationComponent.vue';
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

const nameOpacity = ref(0);
const bgOpacity = ref(0); // 모든 배경 요소에 사용할 통합 투명도 변수
const isCardComplete = ref(false);

// 테두리 애니메이션 상태 변수
const topBorderWidth = ref(0);
const rightBorderHeight = ref(0);
const bottomBorderWidth = ref(0);
const leftBorderHeight = ref(0);

// 파티클 생성 로직 개선 - 각 파티클에 고유한 위치와 애니메이션 속성 부여
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

// 드림 파티클 생성 로직 개선
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

// 파티클 클래스 생성 함수
const getParticleClass = (index: number): string => {
  const classes = ['particle-tiny', 'particle-small', 'particle-medium', 'particle-large'];
  return classes[index % classes.length];
};
// 틸트 효과를 위한 변수
const cardRef = ref<HTMLElement | null>(null);
const tilt = reactive({
  x: 0,
  y: 0,
  active: false,
});

// 마우스 이벤트 핸들러 (틸트 효과)
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

// 순차적 애니메이션 시작 함수
const startCardAnimation = () => {
  // 1단계: 테두리 한붓그리기 애니메이션
  setTimeout(() => {
    drawBorders(() => {
      // 2단계: 배경 요소들 모두 페이드인
      fadeInBackground(() => {
        // 3단계: 텍스트 페이드인
        fadeInName(() => {
          // 애니메이션 완료 상태 설정
          isCardComplete.value = true;

          // 완료 콜백 호출 (필요한 경우)
          if (props.onComplete) {
            props.onComplete();
          }
        });
      });
    });
  }, 300); // 시작 딜레이 시간 단축
};

// 테두리 한붓그리기 애니메이션 (개선된 타이밍)
const drawBorders = (callback: () => void) => {
  // 각 테두리 선별 애니메이션 시간 (ms) - 살짝 빠르게 조정
  const duration = 350;
  const step = 16;
  const steps = duration / step;

  // 이징 함수 추가 (더 자연스러운 움직임)
  const easeOutQuad = (t: number): number => t * (2 - t);

  // 1. 상단 테두리 그리기
  let currentStep = 0;
  const animateTop = () => {
    currentStep++;
    const progress = currentStep / steps;
    const easedProgress = easeOutQuad(Math.min(progress, 1));
    topBorderWidth.value = easedProgress * 100; // 상단 테두리 너비를 0%에서 100%로

    if (currentStep < steps) {
      setTimeout(animateTop, step);
    } else {
      // 2. 우측 테두리 그리기 (상단 테두리가 완료되기 약간 전에 시작)
      currentStep = 0;
      const animateRight = () => {
        currentStep++;
        const progress = currentStep / steps;
        const easedProgress = easeOutQuad(Math.min(progress, 1));
        rightBorderHeight.value = easedProgress * 100; // 우측 테두리 높이를 0%에서 100%로

        if (currentStep < steps) {
          setTimeout(animateRight, step);
        } else {
          // 3. 하단 테두리 그리기 (우측에서 좌측으로)
          currentStep = 0;
          const animateBottom = () => {
            currentStep++;
            const progress = currentStep / steps;
            const easedProgress = easeOutQuad(Math.min(progress, 1));
            bottomBorderWidth.value = easedProgress * 100; // 하단 테두리 너비를 0%에서 100%로

            if (currentStep < steps) {
              setTimeout(animateBottom, step);
            } else {
              // 4. 좌측 테두리 그리기 (하단에서 상단으로)
              currentStep = 0;
              const animateLeft = () => {
                currentStep++;
                const progress = currentStep / steps;
                const easedProgress = easeOutQuad(Math.min(progress, 1));
                leftBorderHeight.value = easedProgress * 100; // 좌측 테두리 높이를 0%에서 100%로

                if (currentStep < steps) {
                  setTimeout(animateLeft, step);
                } else {
                  // 모든 테두리 애니메이션이 완료되면 콜백 호출 (지연 시간 단축)
                  setTimeout(callback, 100);
                }
              };

              animateLeft();
            }
          };

          animateBottom();
        }
      };

      animateRight();
    }
  };

  animateTop();
};

// 배경 애니메이션 (통합 - 부드러운 이징 적용)
const fadeInBackground = (callback: () => void) => {
  const duration = 1000;
  const step = 16;
  const steps = duration / step;
  let currentStep = 0;

  // 이징 함수 추가
  const easeInOutCubic = (t: number): number => {
    return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
  };

  const animate = () => {
    currentStep++;
    const progress = currentStep / steps;
    const easedProgress = easeInOutCubic(Math.min(progress, 1));
    bgOpacity.value = easedProgress;

    if (currentStep < steps) {
      setTimeout(animate, step);
    } else {
      setTimeout(callback, 100);
    }
  };

  animate();
};

// 텍스트 애니메이션 (부드러운 이징 적용)
const fadeInName = (callback: () => void) => {
  const duration = 700; // 시간 약간 단축
  const step = 16;
  const steps = duration / step;
  let currentStep = 0;

  // 이징 함수 추가
  const easeOutCubic = (t: number): number => {
    return 1 - Math.pow(1 - t, 3);
  };

  const animate = () => {
    currentStep++;
    const progress = currentStep / steps;
    const easedProgress = easeOutCubic(Math.min(progress, 1));
    nameOpacity.value = easedProgress;

    if (currentStep < steps) {
      setTimeout(animate, step);
    } else {
      // 텍스트 페이드인 완료 후 즉시 콜백 호출
      callback();
    }
  };

  animate();
};

onMounted(() => {
  isCardComplete.value = false;
  startCardAnimation();
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

        <!-- 3. 카드 테두리 - 한붓그리기 스타일 -->
        <div class="card-border-container absolute top-5 right-5 bottom-5 left-5 z-10">
          <!-- 상단 테두리 (왼쪽 -> 오른쪽) -->
          <div
            class="border-top absolute top-0 left-0 h-0.5 duration-300 ease-out"
            :style="{
              width: `${topBorderWidth}%`,
              background: `linear-gradient(90deg, rgba(139, 92, 246, 0.2), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>

          <!-- 우측 테두리 (상단 -> 하단) -->
          <div
            class="border-right absolute top-0 right-0 w-0.5 duration-300 ease-out"
            :style="{
              height: `${rightBorderHeight}%`,
              background: `linear-gradient(180deg, rgba(139, 92, 246, 0.5), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>

          <!-- 하단 테두리 (오른쪽 -> 왼쪽) -->
          <div
            class="border-bottom absolute right-0 bottom-0 h-0.5 duration-300 ease-out"
            :style="{
              width: `${bottomBorderWidth}%`,
              background: `linear-gradient(270deg, rgba(139, 92, 246, 0.5), rgba(139, 92, 246, 0.5))`,
              boxShadow: `0 0 5px rgba(139, 92, 246, 0.4), 0 0 10px rgba(139, 92, 246, 0.2)`,
            }"
          ></div>

          <!-- 좌측 테두리 (하단 -> 상단) -->
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

        <!-- 수정된 파티클 템플릿 -->

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
        <!-- 유성 효과 - 방향 수정 -->
        <div
          class="shooting-star-1 pointer-events-none absolute h-0.5 w-24 transition-opacity duration-700"
          :style="{ opacity: bgOpacity * 0.9 }"
        ></div>
        <div
          class="shooting-star-2 pointer-events-none absolute h-0.5 w-16 transition-opacity duration-700"
          :style="{ opacity: bgOpacity * 0.7 }"
        ></div>

        <!-- 5. 카드 타이틀 - 마지막으로 나타남 -->
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

        <!-- 카드 하단 텍스트 - 마지막으로 나타남 -->
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
