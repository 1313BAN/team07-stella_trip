<script setup lang="ts">
import { ref, onMounted } from 'vue';
import type { StellaData } from '@/services/api/domains/plan/types';

// Props 추가
interface Props {
  stella?: StellaData | null;
}

const props = defineProps<Props>();

// 북두칠성 기본 별 데이터
const defaultStars = [
  { x: 18, y: 65, size: 2.9, highlight: 0 },
  { x: 37, y: 70, size: 3.1, highlight: 0 },
  { x: 42, y: 45, size: 2.7, highlight: 0 },
  { x: 23, y: 45, size: 2.8, highlight: 0 },
  { x: 55, y: 25, size: 2.6, highlight: 0 },
  { x: 68, y: 12, size: 3.0, highlight: 0 },
  { x: 82, y: 5, size: 2.5, highlight: 0 },
];

// 북두칠성 기본 연결선 데이터
const defaultConnections = [
  { from: 0, to: 1 },
  { from: 1, to: 2 },
  { from: 2, to: 3 },
  { from: 3, to: 0 },
  { from: 2, to: 4 },
  { from: 4, to: 5 },
  { from: 5, to: 6 },
];

// 별 데이터 처리
const stars = ref(defaultStars);
const connections = ref(defaultConnections);

// props에 따라 데이터 업데이트
const updateStarsAndConnections = () => {
  if (!props.stella?.nodes.length) {
    stars.value = [...defaultStars];
    connections.value = [...defaultConnections];
    return;
  }

  // 좌표 정규화
  const nodes = props.stella.nodes;
  const xCoords = nodes.map(n => n.x);
  const yCoords = nodes.map(n => n.y);

  const maxX = Math.max(...xCoords);
  const maxY = Math.max(...yCoords);
  const minX = Math.min(...xCoords);
  const minY = Math.min(...yCoords);

  const xRange = maxX - minX || 1;
  const yRange = maxY - minY || 1;

  // 올바른 좌표 정규화
  stars.value = nodes.map(node => ({
    x: ((node.x - minX) / xRange) * 60 + 20,
    y: ((node.y - minY) / yRange) * 60 + 20,
    size: 2 + Math.random() * 1.5,
    highlight: 0,
  }));

  if (props.stella.edges.length) {
    connections.value = props.stella.edges.map(edge => ({
      from: edge.from,
      to: edge.to,
    }));
  }
};

// 마우스 위치 추적
const mouseX = ref(0);
const mouseY = ref(0);
const isHovering = ref(false);

// 정적 파티클 (애니메이션 없음)
const particles = ref(
  Array(40)
    .fill(null)
    .map(() => ({
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 0.6 + 0.1,
      opacity: Math.random() * 0.6 + 0.1,
      class: getRandomParticleClass(),
    }))
);

const dreamParticles = ref(
  Array(15)
    .fill(null)
    .map(() => ({
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 0.7 + 0.3,
      opacity: Math.random() * 0.7 + 0.3,
    }))
);

// 파티클 클래스 랜덤 생성
function getRandomParticleClass() {
  const classes = [
    'particle-tiny',
    'particle-small',
    'particle-medium',
    'particle-large',
    'particle-extra',
    'particle-huge',
  ];
  return classes[Math.floor(Math.random() * classes.length)];
}

// 마우스 이벤트 핸들러 (호버 효과만 유지)
const trackMouse = (event: MouseEvent) => {
  if (!constellationRef.value) return;

  const rect = constellationRef.value.getBoundingClientRect();
  mouseX.value = ((event.clientX - rect.left) / rect.width) * 100;
  mouseY.value = ((event.clientY - rect.top) / rect.height) * 100;
  isHovering.value = true;

  // 마우스 근처의 별들 하이라이트
  highlightNearbyStars();
};

// 마우스 근처 별들에 반응 추가
const highlightNearbyStars = () => {
  stars.value.forEach(star => {
    const distance = Math.sqrt(
      Math.pow(star.x - mouseX.value, 2) + Math.pow(star.y - mouseY.value, 2)
    );

    // 거리에 따라 하이라이트 강도 조절 (부드러운 감소)
    if (distance < 25) {
      star.highlight = Math.max(0, 1 - distance / 25);
    } else {
      star.highlight = 0;
    }
  });
};

const resetMouse = () => {
  isHovering.value = false;

  // 별 하이라이트 제거
  stars.value.forEach(star => {
    star.highlight = 0;
  });
};

// 정적 배경 별 생성 (애니메이션 없음)
const bgStars = ref<{ x: number; y: number; size: number; opacity: number }[]>([]);

// 초기화 로직
const constellationRef = ref<HTMLElement | null>(null);

onMounted(() => {
  // props에 따라 데이터 업데이트
  updateStarsAndConnections();

  // 정적 배경 별 생성
  for (let i = 0; i < 200; i++) {
    bgStars.value.push({
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 0.5 + 0.1,
      opacity: Math.random() * 0.4 + 0.1,
    });
  }
});
</script>

<template>
  <div
    ref="constellationRef"
    class="constellation-container relative z-100 h-full w-full cursor-pointer overflow-hidden"
    @mousemove="trackMouse"
    @mouseleave="resetMouse"
  >
    <!-- 정적 파티클 효과 (애니메이션 없음) -->
    <div class="floating-particles absolute top-0 left-0 h-full w-full overflow-hidden">
      <!-- 일반 파티클 -->
      <div
        v-for="(particle, i) in particles"
        :key="`particle-${i}`"
        class="particle absolute rounded-full"
        :class="particle.class"
        :style="{
          top: `${particle.y}%`,
          left: `${particle.x}%`,
          width: `${particle.size * 2}px`,
          height: `${particle.size * 2}px`,
          opacity: particle.opacity,
        }"
      ></div>

      <!-- 드림 파티클 -->
      <div
        v-for="(particle, i) in dreamParticles"
        :key="`dream-${i}`"
        class="dream-particle absolute rounded-full"
        :style="{
          top: `${particle.y}%`,
          left: `${particle.x}%`,
          width: `${particle.size * 3}px`,
          height: `${particle.size * 3}px`,
          background: 'rgba(255, 255, 255, 0.5)',
          opacity: particle.opacity * 0.5,
        }"
      ></div>
    </div>

    <!-- 정적 배경 별들 -->
    <div
      v-for="(star, idx) in bgStars"
      :key="`bg-star-${idx}`"
      class="absolute rounded-full bg-white"
      :style="{
        left: `${star.x}%`,
        top: `${star.y}%`,
        width: `${star.size}px`,
        height: `${star.size}px`,
        opacity: isHovering ? star.opacity * 1.3 : star.opacity,
        transition: 'opacity 0.3s ease',
      }"
    ></div>

    <!-- 별자리 SVG -->
    <svg class="absolute inset-0 h-full w-full" viewBox="0 0 100 100">
      <!-- 연결선 (완전히 그려진 상태) -->
      <g class="constellation">
        <path
          v-for="(conn, idx) in connections"
          :key="`conn-${idx}`"
          :d="`M${stars[conn.from]?.x || 0} ${stars[conn.from]?.y || 0} 
              L${stars[conn.to]?.x || 0} ${stars[conn.to]?.y || 0}`"
          stroke="rgba(220, 210, 255, 0.75)"
          stroke-width="0.5"
          fill="none"
          class="constellation-line transition-all duration-300"
          :style="{
            strokeWidth: isHovering ? '0.7' : '0.5',
            strokeOpacity: isHovering ? 0.9 : 0.75,
            filter: 'drop-shadow(0 0 2px rgba(220, 210, 255, 0.8))',
          }"
        />
      </g>

      <!-- 별들 (완전히 표시된 상태) -->
      <g class="star-group">
        <g v-for="(star, idx) in stars" :key="`star-${idx}`">
          <!-- 외부 오라 효과 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(180, 170, 240, ${0.1 + star.highlight * 0.2})`"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 4.5
                : star.size * 3.8
            "
            :style="{
              transition: 'all 0.7s ease',
              filter: 'blur(3px)',
              opacity: star.highlight ? 0.6 + star.highlight * 0.4 : 0.5,
            }"
          />

          <!-- 외부 글로우 효과 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(200, 190, 240, ${0.15 + star.highlight * 0.3})`"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 3.2
                : star.size * 2.8
            "
            :style="{
              transition: 'all 0.7s ease',
              filter: 'blur(2px)',
              opacity: star.highlight ? 0.8 + star.highlight * 0.2 : 0.5,
            }"
          />

          <!-- 중간 글로우 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(230, 220, 255, ${0.5 + star.highlight * 0.4})`"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 1.8
                : star.size * 1.4
            "
            :style="{
              transition: 'all 0.5s ease',
              filter: 'blur(1px)',
              opacity: star.highlight ? 0.9 + star.highlight * 0.1 : 0.7,
            }"
          />

          <!-- 별 중심 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(255, 255, 255, ${1 + star.highlight * 0.3})`"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 0.9
                : star.size * 0.7
            "
            :style="{
              transition: 'all 0.3s ease',
              filter: star.highlight
                ? `brightness(${1.2 + star.highlight * 0.8}) drop-shadow(0 0 ${3 + star.highlight * 4}px white)`
                : 'none',
            }"
          />
        </g>
      </g>
    </svg>

    <!-- 정적 오라 효과 -->
    <div
      class="aura absolute"
      :style="{
        top: '50%',
        left: '50%',
        width: '85%',
        height: '85%',
        borderRadius: '50%',
        transform: 'translate(-50%, -50%)',
        background: 'radial-gradient(circle, rgba(100, 80, 180, 0.1) 0%, transparent 70%)',
        opacity: 0.4,
      }"
    />
  </div>
</template>

<style scoped>
/* 파티클 스타일 (애니메이션 제거) */
.particle {
  background: radial-gradient(circle, rgba(255, 255, 255, 0.9) 0%, transparent 70%);
}

.particle-tiny {
  width: 1px;
  height: 1px;
  background: radial-gradient(circle, rgba(220, 210, 255, 0.7) 0%, transparent 70%);
}

.particle-small {
  width: 2px;
  height: 2px;
}

.particle-medium {
  width: 3px;
  height: 3px;
  background: radial-gradient(circle, rgba(210, 200, 255, 0.8) 0%, transparent 70%);
}

.particle-large {
  width: 4px;
  height: 4px;
  background: radial-gradient(circle, rgba(220, 210, 255, 0.8) 0%, transparent 70%);
}

.particle-extra {
  width: 5px;
  height: 5px;
  background: radial-gradient(circle, rgba(200, 190, 255, 0.7) 0%, transparent 70%);
}

.particle-huge {
  width: 6px;
  height: 6px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.7) 0%, transparent 70%);
}

.constellation-container {
  color: white;
}
</style>
