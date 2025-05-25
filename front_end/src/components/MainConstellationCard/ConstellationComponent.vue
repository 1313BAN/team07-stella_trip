<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

// 별 데이터
const stars = ref([
  { x: 25, y: 30, size: 2.5, pulse: 1, delay: 0, opacity: 0, highlight: 0 },
  { x: 40, y: 20, size: 3, pulse: 1.5, delay: 0.2, opacity: 0, highlight: 0 },
  { x: 55, y: 25, size: 2, pulse: 2, delay: 0.4, opacity: 0, highlight: 0 },
  { x: 70, y: 35, size: 3.5, pulse: 1.3, delay: 0.6, opacity: 0, highlight: 0 },
  { x: 60, y: 50, size: 3, pulse: 1.8, delay: 0.8, opacity: 0, highlight: 0 },
  { x: 40, y: 55, size: 2.5, pulse: 1.2, delay: 1.0, opacity: 0, highlight: 0 },
  { x: 25, y: 65, size: 2, pulse: 1.7, delay: 1.2, opacity: 0, highlight: 0 },
  { x: 35, y: 75, size: 3, pulse: 1.4, delay: 1.4, opacity: 0, highlight: 0 },
  { x: 55, y: 80, size: 2.5, pulse: 1.6, delay: 1.6, opacity: 0, highlight: 0 },
]);

// 별자리 연결선 (연결선 그룹화 및 타이밍 설정)
const connections = ref([
  // 첫 번째 그룹 - 빠르게 시작
  { from: 0, to: 1, progress: 0, group: 0, speed: 1.0 },
  { from: 2, to: 3, progress: 0, group: 0, speed: 0.9 },
  { from: 4, to: 5, progress: 0, group: 0, speed: 1.1 },

  // 두 번째 그룹 - 약간 딜레이 후 시작
  { from: 1, to: 2, progress: 0, group: 1, speed: 0.8 },
  { from: 5, to: 0, progress: 0, group: 1, speed: 1.2 },

  // 세 번째 그룹 - 추가 딜레이 후 시작
  { from: 3, to: 4, progress: 0, group: 2, speed: 1.0 },
  { from: 5, to: 6, progress: 0, group: 2, speed: 0.9 },

  // 네 번째 그룹 - 마지막 연결
  { from: 6, to: 7, progress: 0, group: 3, speed: 1.1 },
  { from: 7, to: 8, progress: 0, group: 3, speed: 0.8 },
]);

// 마우스 위치 추적
const mouseX = ref(0);
const mouseY = ref(0);
const isHovering = ref(false);

// 파티클 관리
const particles = ref(
  Array(40)
    .fill(null)
    .map(() => ({
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 0.6 + 0.1,
      opacity: Math.random() * 0.6 + 0.1,
      twinkleSpeed: Math.random() * 3000 + 1000,
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
      delay: Math.random() * 20,
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

// 마우스 이벤트 핸들러
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

// 배경 별 생성
const bgStars = ref<
  { x: number; y: number; size: number; opacity: number; twinkleSpeed: number }[]
>([]);

// 초기화 및 애니메이션 로직
const constellationRef = ref<HTMLElement | null>(null);
let connectionAnimationTimeouts: number[] = [];

onMounted(() => {
  // 배경 별 무작위 생성 (더 많은 별)
  for (let i = 0; i < 200; i++) {
    bgStars.value.push({
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 0.5 + 0.1, // 약간 더 작은 사이즈
      opacity: Math.random() * 0.4 + 0.1, // 약간 더 은은한 불투명도
      twinkleSpeed: Math.random() * 3000 + 1000,
    });
  }

  // 별 입장 애니메이션 (약간 더 천천히)
  stars.value.forEach((star, index) => {
    setTimeout(() => {
      const fadeIn = () => {
        star.opacity += 0.05;
        if (star.opacity < 1) {
          requestAnimationFrame(fadeIn);
        } else {
          star.opacity = 1;
        }
      };
      fadeIn();
    }, index * 250); // 약간 더 긴 딜레이
  });

  // 연결선 입장 애니메이션
  startConnectionAnimation();
});

// 연결선 애니메이션 시작 (그룹별로 겹쳐서 애니메이션)
const startConnectionAnimation = () => {
  // 그룹별 시작 딜레이 정의 (밀리초)
  const groupDelays = [800, 1000, 1200, 1400];

  // 각 연결선마다 애니메이션 시작
  connections.value.forEach(connection => {
    // 그룹에 따른 기본 딜레이 + 약간의 무작위성
    const randomOffset = Math.random() * 150;
    const baseDelay = groupDelays[connection.group];
    const totalDelay = baseDelay + randomOffset;

    connectionAnimationTimeouts.push(
      window.setTimeout(() => {
        // 각 연결선마다 약간 다른 지속시간 설정
        const baseDuration = 1200;
        const duration = baseDuration / connection.speed; // 속도 계수에 따라 지속시간 조정
        const step = 16;
        const totalSteps = duration / step;
        let currentStep = 0;

        const animateConnection = () => {
          currentStep++;
          // easeOutCubic 이이징 적용
          const progress = currentStep / totalSteps;
          const easedProgress = 1 - Math.pow(1 - progress, 3);

          connection.progress = easedProgress;

          if (currentStep < totalSteps) {
            connectionAnimationTimeouts.push(window.setTimeout(animateConnection, step));
          }
        };

        animateConnection();
      }, totalDelay)
    );
  });
};

// 컴포넌트 제거 시 정리
onUnmounted(() => {
  connectionAnimationTimeouts.forEach(timeout => {
    clearTimeout(timeout);
  });
});
</script>

<template>
  <div
    ref="constellationRef"
    class="constellation-container relative z-100 h-full w-full cursor-pointer overflow-hidden"
    @mousemove="trackMouse"
    @mouseleave="resetMouse"
  >
    <!-- 파티클 효과 -->
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
          animation: `float ${10 + (i % 5)}s linear infinite`,
          animationDelay: `${i * 0.5}s`,
        }"
      ></div>

      <!-- 드림 파티클 (더 크고 희미한 효과) -->
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
          opacity: particle.opacity * 0.5, // 더 은은하게
          animation: 'dream 25s linear infinite',
          animationDelay: `${particle.delay}s`,
        }"
      ></div>
    </div>

    <!-- 배경 별들 -->
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
        transition: `all ${star.twinkleSpeed / 1000}s ease`,
        animation: `twinkle ${star.twinkleSpeed / 1000}s infinite`,
      }"
    ></div>

    <!-- 별자리 SVG -->
    <svg class="absolute inset-0 h-full w-full" viewBox="0 0 100 100">
      <!-- 연결선 -->
      <g class="constellation">
        <path
          v-for="(conn, idx) in connections"
          :key="`conn-${idx}`"
          :d="`M${stars[conn.from].x} ${stars[conn.from].y} 
              L${stars[conn.from].x + (stars[conn.to].x - stars[conn.from].x) * conn.progress} 
               ${stars[conn.from].y + (stars[conn.to].y - stars[conn.from].y) * conn.progress}`"
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

      <!-- 별들 -->
      <g class="star-group">
        <g v-for="(star, idx) in stars" :key="`star-${idx}`">
          <!-- 외부 오라 효과 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(180, 170, 240, ${star.opacity * 0.1 + star.highlight * 0.2})`"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 4.5
                : star.size * 3.8
            "
            :style="{
              transition: 'all 0.7s ease',
              filter: 'blur(3px)',
              opacity: star.highlight ? 0.6 + star.highlight * 0.4 : star.opacity * 0.5,
            }"
          />

          <!-- 외부 글로우 효과 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(200, 190, 240, ${star.opacity * 0.15 + star.highlight * 0.3})`"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 3.2
                : star.size * 2.8
            "
            :style="{
              transition: 'all 0.7s ease',
              filter: 'blur(2px)',
              opacity: star.highlight ? 0.8 + star.highlight * 0.2 : star.opacity * 0.5,
            }"
          />

          <!-- 중간 글로우 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(230, 220, 255, ${star.opacity * 0.5 + star.highlight * 0.4})`"
            class="animate-pulse"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 1.8
                : star.size * 1.4
            "
            :style="{
              animationDuration: `${star.pulse * 2}s`,
              animationDelay: `${star.delay}s`,
              transition: 'all 0.5s ease',
              filter: 'blur(1px)',
              opacity: star.highlight ? 0.9 + star.highlight * 0.1 : star.opacity * 0.7,
            }"
          />

          <!-- 별 중심 -->
          <circle
            :cx="star.x"
            :cy="star.y"
            :fill="`rgba(255, 255, 255, ${star.opacity + star.highlight * 0.3})`"
            class="animate-pulse"
            :r="
              isHovering && Math.abs(mouseX - star.x) < 15 && Math.abs(mouseY - star.y) < 15
                ? star.size * 0.9
                : star.size * 0.7
            "
            :style="{
              animationDuration: `${star.pulse}s`,
              animationDelay: `${star.delay}s`,
              transition: 'all 0.3s ease',
              filter: star.highlight
                ? `brightness(${1.2 + star.highlight * 0.8}) drop-shadow(0 0 ${3 + star.highlight * 4}px white)`
                : 'none',
            }"
          />
        </g>
      </g>
    </svg>

    <!-- 오라 효과 -->
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
        animation: 'pulse 6s ease-in-out infinite',
      }"
    ></div>
  </div>
</template>

<style scoped>
@keyframes twinkle {
  0%,
  100% {
    opacity: var(--opacity, 0.5);
    transform: scale(1);
  }
  50% {
    opacity: calc(var(--opacity, 0.5) * 0.5);
    transform: scale(0.8);
  }
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

@keyframes dream {
  0% {
    opacity: 0;
    transform: scale(1);
  }
  20% {
    opacity: 0.8;
    transform: scale(2);
  }
  80% {
    opacity: 0.8;
    transform: scale(1.5);
  }
  100% {
    opacity: 0;
    transform: scale(0.5);
  }
}

@keyframes glow {
  0%,
  100% {
    stroke-opacity: 0.5;
    filter: drop-shadow(0 0 6px rgba(220, 210, 255, 0.4));
  }
  50% {
    stroke-opacity: 0.8;
    filter: drop-shadow(0 0 10px rgba(220, 210, 255, 0.6));
  }
}

/* 별자리 스타일 */
.constellation-line {
  animation: glow 4s ease-in-out infinite;
}

/* 파티클 스타일 */
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
