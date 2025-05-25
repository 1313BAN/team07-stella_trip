<template>
  <div
    ref="cardRef"
    class="tarot-card-container relative h-96 w-80 cursor-pointer"
    @mousemove="handleMouseMove"
    @mouseleave="handleMouseLeave"
    @click="handleClick"
  >
    <div class="card-flip-inner" :class="{ flipped: isFlipped }">
      <!-- Front Side -->
      <div class="card-side card-front">
        <div class="tarot-card relative h-96 w-80 overflow-hidden" :style="cardStyle">
          <!-- Milky Way -->
          <div
            class="milky-way absolute"
            :style="{
              top: '-50%',
              left: '-50%',
              width: '200%',
              height: '200%',
              borderRadius: '40%',
            }"
          ></div>

          <!-- Card Border -->
          <div
            class="card-border border-opacity-20 absolute top-5 right-5 bottom-5 left-5 rounded-lg border border-purple-400"
            :style="{ background: 'rgba(79, 70, 229, 0.01)' }"
          ></div>

          <!-- Title -->
          <div
            class="absolute top-8 left-1/2 z-10 -translate-x-1/2 transform font-semibold text-slate-100 uppercase"
            :style="titleStyle"
          >
            {{ cardData.frontTitle }}
          </div>

          <!-- Shooting Star -->
          <div
            class="shooting-stars pointer-events-none absolute top-0 left-0 h-full w-full overflow-hidden"
          >
            <div class="shooting-star absolute h-0.5 w-20"></div>
          </div>

          <!-- Floating Particles -->
          <div
            class="floating-particles pointer-events-none absolute top-0 left-0 h-full w-full overflow-hidden"
          >
            <div
              v-for="(_, i) in particles"
              :key="i"
              :class="getParticleClass(i)"
              class="particle absolute rounded-full"
            ></div>
          </div>

          <!-- Dream Particles -->
          <div class="dream-particles pointer-events-none absolute top-0 left-0 h-full w-full">
            <div
              v-for="(_, i) in dreamParticles"
              :key="`dream-${i}`"
              class="dream-particle absolute rounded-full"
            ></div>
          </div>

          <!-- Aura -->
          <div
            class="aura absolute h-80 w-80 rounded-full"
            :style="{
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
            }"
          ></div>

          <!-- Constellation -->
          <div
            class="constellation-container absolute top-1/2 left-1/2 h-96 w-96 -translate-x-1/2 -translate-y-1/2 transform"
          >
            <svg
              class="constellation h-full w-full"
              viewBox="0 0 420 420"
              xmlns="http://www.w3.org/2000/svg"
            >
              <!-- Constellation Lines -->
              <line
                v-for="(line, i) in processedLines"
                :key="`line-${i}`"
                class="constellation-line"
                :x1="line.x1"
                :y1="line.y1"
                :x2="line.x2"
                :y2="line.y2"
              />

              <!-- Stars -->
              <g v-for="(star, i) in processedStars" :key="`star-${i}`" class="star-group">
                <circle
                  v-for="(circle, j) in getStarCircles(star)"
                  :key="`circle-${j}`"
                  :cx="star.cx"
                  :cy="star.cy"
                  :r="circle.radius"
                  :fill="circle.color"
                  :opacity="circle.opacity"
                />
              </g>
            </svg>
          </div>

          <!-- Bottom Text -->
          <div
            class="absolute bottom-8 left-1/2 z-10 -translate-x-1/2 transform text-center font-medium text-slate-100"
            :style="bottomTextStyle"
          >
            ✦ {{ cardData.constellation }} ✦
          </div>

          <!-- Card Filter -->
          <div
            class="card-filter pointer-events-none absolute top-0 left-0 z-50 h-full w-full"
          ></div>
        </div>
      </div>

      <!-- Back Side -->
      <div class="card-side card-back">
        <div class="tarot-card relative h-96 w-80 overflow-hidden" :style="cardBackStyle">
          <!-- Background Effects for Back -->
          <div
            class="back-milky-way absolute"
            :style="{
              top: '-50%',
              left: '-50%',
              width: '200%',
              height: '200%',
              borderRadius: '40%',
            }"
          ></div>

          <!-- Card Border -->
          <div
            class="card-border border-opacity-20 absolute top-5 right-5 bottom-5 left-5 rounded-lg border border-purple-400"
            :style="{ background: 'rgba(79, 70, 229, 0.01)' }"
          ></div>

          <!-- Title -->
          <div
            class="absolute top-8 left-1/2 z-10 -translate-x-1/2 transform font-semibold text-slate-100 uppercase"
            :style="titleStyle"
          >
            {{ cardData.backTitle }}
          </div>

          <!-- Description Content -->
          <div class="absolute top-20 right-5 bottom-16 left-5 z-10 overflow-y-auto p-4">
            <div class="text-sm leading-relaxed text-slate-100" :style="descriptionStyle">
              <p class="mb-4">
                <strong class="text-indigo-300">키워드:</strong>
                {{ cardData.keywords }}
              </p>

              <p class="mb-4">
                {{ cardData.description }}
              </p>

              <p class="mb-4">
                <strong class="text-violet-300">정위치:</strong>
                <br />
                {{ cardData.uprightMeaning }}
              </p>

              <p v-if="cardData.reversedMeaning" class="mb-4">
                <strong class="text-rose-300">역위치:</strong>
                <br />
                {{ cardData.reversedMeaning }}
              </p>
            </div>
          </div>

          <!-- Bottom Text -->
          <div
            class="absolute bottom-8 left-1/2 z-10 -translate-x-1/2 transform text-center font-medium text-slate-100"
            :style="bottomTextStyle"
          >
            ✦
          </div>

          <!-- Card Filter -->
          <div
            class="card-filter pointer-events-none absolute top-0 left-0 z-50 h-full w-full"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, type CSSProperties } from 'vue';
import type { StellaData } from '@/services/api/domains/plan/types';

interface TarotCardData {
  frontTitle: string;
  backTitle: string;
  constellation: string;
  keywords: string;
  description: string;
  uprightMeaning: string;
  reversedMeaning?: string;
}

interface ProcessedStar {
  cx: number;
  cy: number;
  size: 'small' | 'medium' | 'large';
}

interface ProcessedLine {
  x1: number;
  y1: number;
  x2: number;
  y2: number;
}

interface Circle {
  radius: number;
  color: string;
  opacity: number;
}

interface Props {
  stella?: StellaData | null;
  cardData: TarotCardData;
}

const props = defineProps<Props>();

const cardRef = ref<HTMLDivElement>();
const isFlipped = ref(false);

// 별자리 데이터를 타로 카드 스타일로 변환
const processedStars = computed((): ProcessedStar[] => {
  if (!props.stella?.nodes.length) {
    // 기본 별자리 패턴 (Hunter) - 중앙에 더 집중
    return [
      { cx: 150, cy: 120, size: 'small' },
      { cx: 270, cy: 120, size: 'small' },
      { cx: 150, cy: 280, size: 'small' },
      { cx: 270, cy: 280, size: 'small' },
      { cx: 180, cy: 150, size: 'medium' },
      { cx: 240, cy: 140, size: 'medium' },
      { cx: 170, cy: 210, size: 'medium' },
      { cx: 250, cy: 210, size: 'medium' },
      { cx: 210, cy: 240, size: 'large' },
      { cx: 200, cy: 300, size: 'large' },
    ];
  }

  // 좌표를 420x420 SVG 좌표계로 정규화
  const nodes = props.stella.nodes;
  const xCoords = nodes.map(n => n.x);
  const yCoords = nodes.map(n => n.y);

  const maxX = Math.max(...xCoords);
  const maxY = Math.max(...yCoords);
  const minX = Math.min(...xCoords);
  const minY = Math.min(...yCoords);

  const xRange = maxX - minX || 1;
  const yRange = maxY - minY || 1;

  return nodes.map((node, index): ProcessedStar => {
    const cx = ((node.x - minX) / xRange) * 200 + 110; // 110-310 범위로 정규화 (중앙에 더 집중)
    const cy = ((node.y - minY) / yRange) * 200 + 110;

    // 별 크기를 인덱스나 위치에 따라 결정
    let size: 'small' | 'medium' | 'large';
    if (index % 3 === 0) size = 'large';
    else if (index % 2 === 0) size = 'medium';
    else size = 'small';

    return { cx, cy, size };
  });
});

const processedLines = computed((): ProcessedLine[] => {
  if (!props.stella?.edges.length || !props.stella?.nodes.length) {
    // 기본 연결선 패턴 - 중앙 집중형
    return [
      { x1: 150, y1: 120, x2: 180, y2: 150 },
      { x1: 180, y1: 150, x2: 240, y2: 140 },
      { x1: 240, y1: 140, x2: 270, y2: 120 },
      { x1: 180, y1: 150, x2: 170, y2: 210 },
      { x1: 240, y1: 140, x2: 250, y2: 210 },
      { x1: 170, y1: 210, x2: 210, y2: 240 },
      { x1: 210, y1: 240, x2: 250, y2: 210 },
      { x1: 170, y1: 210, x2: 150, y2: 280 },
      { x1: 210, y1: 240, x2: 200, y2: 300 },
      { x1: 250, y1: 210, x2: 270, y2: 280 },
    ];
  }

  const stars = processedStars.value;
  return props.stella.edges
    .filter(edge => edge.from < stars.length && edge.to < stars.length)
    .map(edge => ({
      x1: stars[edge.from].cx,
      y1: stars[edge.from].cy,
      x2: stars[edge.to].cx,
      y2: stars[edge.to].cy,
    }));
});

const cardStyle = computed(
  (): CSSProperties => ({
    width: '300px',
    height: '500px',
    background: `
    linear-gradient(135deg, 
      rgba(79, 70, 229, 0.1) 0%,
      rgba(139, 92, 246, 0.08) 20%,
      rgba(219, 39, 119, 0.06) 40%,
      rgba(59, 130, 246, 0.05) 60%,
      rgba(139, 92, 246, 0.08) 80%,
      rgba(79, 70, 229, 0.1) 100%
    ),
    radial-gradient(ellipse at 30% 20%, rgba(79, 70, 229, 0.08) 0%, transparent 60%),
    radial-gradient(ellipse at 70% 80%, rgba(139, 92, 246, 0.06) 0%, transparent 50%),
    linear-gradient(180deg, #0a0a1a 0%, #1a1a2e 30%, #0f0f23 70%, #000000 100%)
  `,
    borderRadius: '15px',
    border: '2px solid rgba(139, 92, 246, 0.2)',
    boxShadow: `
    0 0 40px rgba(79, 70, 229, 0.15),
    inset 0 0 60px rgba(139, 92, 246, 0.03)
  `,
    fontFamily: "'Cinzel', serif",
  })
);

const cardBackStyle = computed(
  (): CSSProperties => ({
    width: '300px',
    height: '500px',
    background: `
    linear-gradient(135deg, 
      rgba(139, 92, 246, 0.12) 0%,
      rgba(79, 70, 229, 0.1) 20%,
      rgba(59, 130, 246, 0.08) 40%,
      rgba(219, 39, 119, 0.06) 60%,
      rgba(79, 70, 229, 0.1) 80%,
      rgba(139, 92, 246, 0.12) 100%
    ),
    radial-gradient(ellipse at 70% 20%, rgba(139, 92, 246, 0.1) 0%, transparent 60%),
    radial-gradient(ellipse at 30% 80%, rgba(79, 70, 229, 0.08) 0%, transparent 50%),
    linear-gradient(180deg, #1a0a2e 0%, #2a1a3e 30%, #1f0f33 70%, #0a0a1a 100%)
  `,
    borderRadius: '15px',
    border: '2px solid rgba(139, 92, 246, 0.2)',
    boxShadow: `
    0 0 40px rgba(79, 70, 229, 0.15),
    inset 0 0 60px rgba(139, 92, 246, 0.03)
  `,
    fontFamily: "'Cinzel', serif",
  })
);

const titleStyle = computed(
  (): CSSProperties => ({
    fontSize: '18px',
    letterSpacing: '3px',
    textShadow: `
    0 0 20px rgba(241, 245, 249, 0.8),
    0 0 40px rgba(139, 92, 246, 0.6),
    0 2px 4px rgba(0, 0, 0, 0.8)
  `,
  })
);

const bottomTextStyle = computed(
  (): CSSProperties => ({
    fontSize: '14px',
    letterSpacing: '2px',
    textShadow: `
    0 0 15px rgba(241, 245, 249, 0.7),
    0 0 30px rgba(165, 180, 252, 0.5),
    0 2px 4px rgba(0, 0, 0, 0.8)
  `,
  })
);

const descriptionStyle = computed(
  (): CSSProperties => ({
    textShadow: `
    0 0 10px rgba(241, 245, 249, 0.5),
    0 1px 2px rgba(0, 0, 0, 0.8)
  `,
  })
);

const particles = ref(Array(30).fill(null));
const dreamParticles = ref(Array(10).fill(null));

const getParticleClass = (index: number): string => {
  const classes = [
    'particle-tiny',
    'particle-small',
    'particle-medium',
    'particle-large',
    'particle-extra',
    'particle-huge',
  ];
  return classes[index % classes.length];
};

const getStarCircles = (star: ProcessedStar): Circle[] => {
  if (star.size === 'small') {
    return [
      { radius: 15, color: 'rgba(139, 92, 246, 0.1)', opacity: 0.4 },
      { radius: 10, color: 'rgba(199, 210, 254, 0.2)', opacity: 0.6 },
      { radius: 6, color: 'rgba(221, 214, 254, 0.4)', opacity: 0.8 },
      { radius: 2, color: '#ffffff', opacity: 1 },
    ];
  } else if (star.size === 'medium') {
    return [
      { radius: 20, color: 'rgba(79, 70, 229, 0.08)', opacity: 0.3 },
      { radius: 14, color: 'rgba(139, 92, 246, 0.15)', opacity: 0.5 },
      { radius: 9, color: 'rgba(199, 210, 254, 0.3)', opacity: 0.7 },
      { radius: 5, color: 'rgba(221, 214, 254, 0.6)', opacity: 0.9 },
      { radius: 2.5, color: '#ffffff', opacity: 1 },
    ];
  } else {
    return [
      { radius: 30, color: 'rgba(79, 70, 229, 0.06)', opacity: 0.25 },
      { radius: 22, color: 'rgba(139, 92, 246, 0.12)', opacity: 0.4 },
      { radius: 16, color: 'rgba(199, 210, 254, 0.2)', opacity: 0.6 },
      { radius: 11, color: 'rgba(221, 214, 254, 0.4)', opacity: 0.8 },
      { radius: 6, color: 'rgba(243, 244, 246, 0.7)', opacity: 0.95 },
      { radius: 3, color: '#ffffff', opacity: 1 },
    ];
  }
};

const handleMouseMove = (e: MouseEvent) => {
  if (!cardRef.value || isFlipped.value) return;

  const rect = cardRef.value.getBoundingClientRect();
  const cardWidth = rect.width;
  const cardHeight = rect.height;

  const mouseX = (e.clientX - rect.left) / cardWidth - 0.5;
  const mouseY = (e.clientY - rect.top) / cardHeight - 0.5;

  const rotateX = mouseY * -35;
  const rotateY = mouseX * 35;

  const inner = cardRef.value.querySelector('.card-flip-inner') as HTMLElement;
  if (inner) {
    inner.style.transform = `rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale(1.05)`;
  }
};

const handleMouseLeave = () => {
  if (!cardRef.value || isFlipped.value) return;

  const inner = cardRef.value.querySelector('.card-flip-inner') as HTMLElement;
  if (inner) {
    inner.style.transform = 'rotateX(0deg) rotateY(0deg) scale(1)';
  }
};

const handleClick = () => {
  isFlipped.value = !isFlipped.value;

  // Reset any mouse move transforms
  if (cardRef.value) {
    const inner = cardRef.value.querySelector('.card-flip-inner') as HTMLElement;
    if (inner) {
      inner.style.transform = '';
    }
  }
};

onMounted(() => {
  // Load Google Fonts
  const link = document.createElement('link');
  link.href = 'https://fonts.googleapis.com/css2?family=Cinzel:wght@400;600&display=swap';
  link.rel = 'stylesheet';
  document.head.appendChild(link);
});
</script>

<style scoped>
/* Card Container and Flip Effect */
.tarot-card-container {
  perspective: 1000px;
}

.card-flip-inner {
  position: relative;
  width: 100%;
  height: 100%;
  transition: transform 0.8s ease-in-out;
  transform-style: preserve-3d;
}

.card-flip-inner.flipped {
  transform: rotateY(180deg);
}

.card-side {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
}

.card-front {
  z-index: 2;
}

.card-back {
  transform: rotateY(180deg);
  z-index: 1;
}

/* Tarot Card */
.tarot-card {
  border-radius: 15px;
  border: 2px solid rgba(139, 92, 246, 0.2);
}

/* Card Border */
.card-border {
  border: 1px solid rgba(139, 92, 246, 0.2);
  border-radius: 10px;
  background: rgba(79, 70, 229, 0.01);
}

/* Card Filter */
.card-filter {
  background: rgba(10, 10, 26, 0.15);
  filter: blur(0.5px);
}

/* Milky Way */
.milky-way {
  background:
    radial-gradient(
      ellipse 400% 100% at 50% 30%,
      rgba(139, 92, 246, 0.12) 0%,
      rgba(219, 39, 119, 0.08) 30%,
      rgba(59, 130, 246, 0.06) 50%,
      transparent 80%
    ),
    linear-gradient(
      45deg,
      transparent 0%,
      rgba(139, 92, 246, 0.04) 20%,
      rgba(219, 39, 119, 0.06) 50%,
      rgba(59, 130, 246, 0.04) 80%,
      transparent 100%
    );
  transform: rotate(-20deg);
  opacity: 0.9;
  animation: milkyWayShift 12s ease-in-out infinite;
}

.back-milky-way {
  background:
    radial-gradient(
      ellipse 400% 100% at 50% 70%,
      rgba(79, 70, 229, 0.15) 0%,
      rgba(139, 92, 246, 0.1) 30%,
      rgba(59, 130, 246, 0.08) 50%,
      transparent 80%
    ),
    linear-gradient(
      -45deg,
      transparent 0%,
      rgba(79, 70, 229, 0.06) 20%,
      rgba(139, 92, 246, 0.08) 50%,
      rgba(59, 130, 246, 0.06) 80%,
      transparent 100%
    );
  transform: rotate(20deg);
  opacity: 0.8;
  animation: milkyWayShift 15s ease-in-out infinite;
}

@keyframes milkyWayShift {
  0%,
  100% {
    opacity: 0.9;
    transform: rotate(-25deg) scale(0.95);
  }
  50% {
    opacity: 1;
    transform: rotate(-15deg) scale(1.1);
  }
}

/* Constellation */
.constellation {
  filter: drop-shadow(0 0 3px rgba(139, 92, 246, 0.3));
}

.constellation-line {
  stroke: #c7d2fe;
  stroke-width: 1.5;
  stroke-opacity: 0.6;
  filter: drop-shadow(0 0 8px rgba(199, 210, 254, 0.5));
  animation: glow 3s ease-in-out infinite;
}

@keyframes glow {
  0%,
  100% {
    stroke-opacity: 0.4;
    filter: drop-shadow(0 0 6px rgba(199, 210, 254, 0.3));
  }
  50% {
    stroke-opacity: 0.8;
    filter: drop-shadow(0 0 12px rgba(199, 210, 254, 0.7));
  }
}

.star-group {
  filter: blur(0.3px);
}

/* Particles */
.particle {
  background: radial-gradient(circle, rgba(255, 255, 255, 0.8) 0%, transparent 70%);
  animation: float 12s linear infinite;
}

.particle-tiny {
  width: 1px;
  height: 1px;
  background: radial-gradient(circle, rgba(199, 210, 254, 0.7) 0%, transparent 70%);
}

.particle-small {
  width: 2px;
  height: 2px;
}

.particle-medium {
  width: 3px;
  height: 3px;
  background: radial-gradient(circle, rgba(199, 210, 254, 0.9) 0%, transparent 70%);
}

.particle-large {
  width: 4px;
  height: 4px;
  background: radial-gradient(circle, rgba(221, 214, 254, 0.8) 0%, transparent 70%);
}

.particle-extra {
  width: 5px;
  height: 5px;
  background: radial-gradient(circle, rgba(165, 180, 252, 0.7) 0%, transparent 70%);
}

.particle-huge {
  width: 6px;
  height: 6px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.6) 0%, transparent 70%);
}

@keyframes float {
  0% {
    opacity: 0;
    transform: translateY(0) translateX(0) rotate(0deg);
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    opacity: 0;
    transform: translateY(-40px) translateX(15px) rotate(360deg);
  }
}

/* Aura */
.aura {
  background: radial-gradient(circle, rgba(79, 70, 229, 0.08) 0%, transparent 70%);
  animation: pulse 5s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.4;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.15);
    opacity: 0.7;
  }
}

/* Shooting Star */
.shooting-star {
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0.6) 0%,
    rgba(255, 255, 255, 0.3) 50%,
    rgba(255, 255, 255, 0) 100%
  );
  animation: shootStar 5s linear infinite;
  transform: rotate(-45deg);
  transform-origin: left center;
}

@keyframes shootStar {
  0% {
    left: calc(100% + 50px);
    top: -50px;
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  30% {
    opacity: 1;
  }
  40% {
    opacity: 0;
  }
  100% {
    left: -130px;
    top: calc(100% + 50px);
    opacity: 0;
  }
}

/* Dream Particles */
.dream-particle {
  width: 1px;
  height: 1px;
  background: rgba(255, 255, 255, 0.6);
  animation: dream 25s linear infinite;
}

@keyframes dream {
  0% {
    opacity: 0;
    transform: scale(1);
  }
  20% {
    opacity: 1;
    transform: scale(2);
  }
  80% {
    opacity: 1;
    transform: scale(1.5);
  }
  100% {
    opacity: 0;
    transform: scale(0.5);
  }
}

/* Particle positions */
.particle:nth-child(1) {
  top: 5%;
  left: 10%;
  animation-delay: 0s;
  animation-duration: 8s;
}
.particle:nth-child(2) {
  top: 15%;
  left: 90%;
  animation-delay: 1s;
  animation-duration: 12s;
}
.particle:nth-child(3) {
  top: 25%;
  left: 5%;
  animation-delay: 2s;
  animation-duration: 10s;
}
.particle:nth-child(4) {
  top: 35%;
  left: 95%;
  animation-delay: 3s;
  animation-duration: 14s;
}
.particle:nth-child(5) {
  top: 45%;
  left: 15%;
  animation-delay: 4s;
  animation-duration: 9s;
}
.particle:nth-child(6) {
  top: 55%;
  left: 85%;
  animation-delay: 5s;
  animation-duration: 11s;
}
.particle:nth-child(7) {
  top: 65%;
  left: 10%;
  animation-delay: 6s;
  animation-duration: 13s;
}
.particle:nth-child(8) {
  top: 75%;
  left: 90%;
  animation-delay: 7s;
  animation-duration: 7s;
}
.particle:nth-child(9) {
  top: 85%;
  left: 25%;
  animation-delay: 8s;
  animation-duration: 15s;
}
.particle:nth-child(10) {
  top: 95%;
  left: 75%;
  animation-delay: 9s;
  animation-duration: 10s;
}
.particle:nth-child(11) {
  top: 20%;
  left: 30%;
  animation-delay: 10s;
  animation-duration: 11s;
}
.particle:nth-child(12) {
  top: 40%;
  left: 70%;
  animation-delay: 11s;
  animation-duration: 9s;
}
.particle:nth-child(13) {
  top: 60%;
  left: 40%;
  animation-delay: 12s;
  animation-duration: 13s;
}
.particle:nth-child(14) {
  top: 80%;
  left: 60%;
  animation-delay: 13s;
  animation-duration: 8s;
}
.particle:nth-child(15) {
  top: 10%;
  left: 50%;
  animation-delay: 14s;
  animation-duration: 12s;
}
.particle:nth-child(16) {
  top: 30%;
  left: 20%;
  animation-delay: 15s;
  animation-duration: 14s;
}
.particle:nth-child(17) {
  top: 50%;
  left: 80%;
  animation-delay: 16s;
  animation-duration: 10s;
}
.particle:nth-child(18) {
  top: 70%;
  left: 30%;
  animation-delay: 17s;
  animation-duration: 11s;
}
.particle:nth-child(19) {
  top: 90%;
  left: 45%;
  animation-delay: 18s;
  animation-duration: 9s;
}
.particle:nth-child(20) {
  top: 12%;
  left: 65%;
  animation-delay: 19s;
  animation-duration: 13s;
}
.particle:nth-child(21) {
  top: 32%;
  left: 35%;
  animation-delay: 20s;
  animation-duration: 8s;
}
.particle:nth-child(22) {
  top: 52%;
  left: 55%;
  animation-delay: 21s;
  animation-duration: 12s;
}
.particle:nth-child(23) {
  top: 72%;
  left: 15%;
  animation-delay: 22s;
  animation-duration: 14s;
}
.particle:nth-child(24) {
  top: 92%;
  left: 85%;
  animation-delay: 23s;
  animation-duration: 10s;
}
.particle:nth-child(25) {
  top: 8%;
  left: 25%;
  animation-delay: 24s;
  animation-duration: 11s;
}
.particle:nth-child(26) {
  top: 28%;
  left: 75%;
  animation-delay: 25s;
  animation-duration: 9s;
}
.particle:nth-child(27) {
  top: 48%;
  left: 45%;
  animation-delay: 26s;
  animation-duration: 13s;
}
.particle:nth-child(28) {
  top: 68%;
  left: 55%;
  animation-delay: 27s;
  animation-duration: 8s;
}
.particle:nth-child(29) {
  top: 88%;
  left: 35%;
  animation-delay: 28s;
  animation-duration: 15s;
}
.particle:nth-child(30) {
  top: 18%;
  left: 85%;
  animation-delay: 29s;
  animation-duration: 12s;
}

/* Dream particle positions */
.dream-particle:nth-child(1) {
  top: 15%;
  left: 20%;
  animation-delay: 0s;
}
.dream-particle:nth-child(2) {
  top: 35%;
  left: 80%;
  animation-delay: 3s;
}
.dream-particle:nth-child(3) {
  top: 55%;
  left: 40%;
  animation-delay: 6s;
}
.dream-particle:nth-child(4) {
  top: 75%;
  left: 60%;
  animation-delay: 9s;
}
.dream-particle:nth-child(5) {
  top: 25%;
  left: 30%;
  animation-delay: 12s;
}
.dream-particle:nth-child(6) {
  top: 45%;
  left: 70%;
  animation-delay: 15s;
}
.dream-particle:nth-child(7) {
  top: 65%;
  left: 25%;
  animation-delay: 18s;
}
.dream-particle:nth-child(8) {
  top: 85%;
  left: 75%;
  animation-delay: 21s;
}
.dream-particle:nth-child(9) {
  top: 40%;
  left: 10%;
  animation-delay: 24s;
}
.dream-particle:nth-child(10) {
  top: 60%;
  left: 90%;
  animation-delay: 27s;
}
</style>
