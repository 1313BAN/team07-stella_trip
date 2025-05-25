<script setup lang="ts">
import { computed } from 'vue';
import type {
  RawNode,
  StellaEdge,
  StellaNode,
  BackgroundStar,
} from '@/services/api/domains/plan/types';

interface Props {
  nodes?: RawNode[];
  edges?: StellaEdge[];
  backgroundStars?: BackgroundStar[];
  isHovered?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  nodes: () => [],
  edges: () => [],
  backgroundStars: () => [],
  isHovered: false,
});

// 노드를 정규화하고 랜덤값을 추가하여 별로 변환
const processedStars = computed((): StellaNode[] => {
  if (props.nodes.length === 0) return [];

  // 좌표의 최대값을 구해서 0-100 범위로 정규화
  const xCoords = props.nodes.map(n => n.x);
  const yCoords = props.nodes.map(n => n.y);

  const maxX = Math.max(...xCoords);
  const maxY = Math.max(...yCoords);
  const minX = Math.min(...xCoords);
  const minY = Math.min(...yCoords);

  // 0으로 나누는 것을 방지
  const xRange = maxX - minX || 1;
  const yRange = maxY - minY || 1;

  return props.nodes.map(
    (node): StellaNode => ({
      x: ((node.x - minX) / xRange) * 80 + 10, // 10-90 범위로 정규화
      y: ((node.y - minY) / yRange) * 80 + 10,
      r: Math.random() * 1.5 + 1, // 1-2.5 범위
      brightness: Math.random() * 0.3 + 0.7, // 0.7-1.0 범위
      delay: Math.random() * 2, // 0-2초 범위
    })
  );
});

// 유효한 연결만 필터링
const validConnections = computed(() =>
  props.edges.filter(
    connection =>
      connection.from < props.nodes.length &&
      connection.to < props.nodes.length &&
      connection.from >= 0 &&
      connection.to >= 0
  )
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
        v-for="(star, index) in backgroundStars"
        :key="`bg-star-${index}`"
        :cx="star.x"
        :cy="star.y"
        :r="star.r"
        fill="currentColor"
        :opacity="star.brightness"
        filter="url(#backgroundGlow)"
        class="animate-pulse"
        :style="{
          animationDuration: `${star.duration}s`,
        }"
      />
    </g>

    <!-- 연결선들 -->
    <g v-if="validConnections.length > 0">
      <line
        v-for="(connection, index) in validConnections"
        :key="`connection-${index}`"
        :x1="processedStars[connection.from]?.x ?? 0"
        :y1="processedStars[connection.from]?.y ?? 0"
        :x2="processedStars[connection.to]?.x ?? 0"
        :y2="processedStars[connection.to]?.y ?? 0"
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
