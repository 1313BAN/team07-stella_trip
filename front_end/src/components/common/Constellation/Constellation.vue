<script setup lang="ts">
import { computed } from 'vue';

interface Star {
  x: number;
  y: number;
  r?: number;
  brightness?: number;
  delay?: number;
  duration?: number;
}

interface Connection {
  from: number;
  to: number;
}

interface Props {
  stars: Star[];
  connections: Connection[];
  backgroundStars: Star[];
  isHovered: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  stars: () => [],
  connections: () => [],
  backgroundStars: () => [],
  isHovered: false,
});

// Star 객체의 기본값을 처리하는 computed
const processedStars = computed(() =>
  props.stars.map(star => ({
    ...star,
    r: star.r ?? 1,
    brightness: star.brightness ?? 1,
    delay: star.delay ?? 1,
    duration: star.duration ?? 1,
  }))
);

const processedBackgroundStars = computed(() =>
  props.backgroundStars.map(star => ({
    ...star,
    r: star.r ?? 1,
    brightness: star.brightness ?? 1,
    delay: star.delay ?? 1,
    duration: star.duration ?? 1,
  }))
);
</script>

<template>
  <!-- 별자리 SVG -->
  <svg
    class="h-full w-full text-purple-200"
    viewBox="0 0 100 100"
    preserveAspectRatio="xMidYMid meet"
  >
    <defs>
      <!-- 글로우 이펙트 -->
      <filter id="glow" x="-50%" y="-50%" width="200%" height="200%">
        <feGaussianBlur stdDeviation="1.5" result="coloredBlur" />
        <feMerge>
          <feMergeNode in="coloredBlur" />
          <feMergeNode in="SourceGraphic" />
        </feMerge>
      </filter>

      <!-- 배경별 글로우 -->
      <filter id="backgroundGlow" x="-100%" y="-100%" width="300%" height="300%">
        <feGaussianBlur stdDeviation="0.8" result="coloredBlur" />
        <feMerge>
          <feMergeNode in="coloredBlur" />
          <feMergeNode in="SourceGraphic" />
        </feMerge>
      </filter>
    </defs>

    <!-- 우주 배경별들 -->
    <g opacity="0.7">
      <circle
        v-for="(star, index) in processedBackgroundStars"
        :key="`bg-star-${index}`"
        :cx="star.x"
        :cy="star.y"
        :r="star.r"
        fill="currentColor"
        :opacity="star.brightness"
        filter="url(#backgroundGlow)"
        class="animate-pulse"
        :style="{
          animationDelay: `${star.delay}s`,
          animationDuration: `${star.duration}s`,
        }"
      />
    </g>

    <!-- 연결선들 -->
    <g>
      <line
        v-for="(connection, index) in connections"
        :key="`connection-${index}`"
        :x1="processedStars[connection.from].x ?? 0"
        :y1="processedStars[connection.from].y ?? 0"
        :x2="processedStars[connection.to].x ?? 0"
        :y2="processedStars[connection.to].y ?? 0"
        stroke="currentColor"
        stroke-width="1"
        opacity="0.7"
        filter="url(#glow)"
        :stroke-dasharray="isHovered ? '0' : '2,1'"
        class="transition-all duration-700"
      />
    </g>

    <!-- 별자리 메인 별들 -->
    <g>
      <circle
        v-for="(star, index) in processedStars"
        :key="`star-${index}`"
        :cx="star.x"
        :cy="star.y"
        :r="star.r"
        fill="currentColor"
        :opacity="star.brightness"
        filter="url(#glow)"
        class="animate-pulse"
        :style="{ animationDelay: `${star.delay}s` }"
      />
    </g>
  </svg>
</template>
