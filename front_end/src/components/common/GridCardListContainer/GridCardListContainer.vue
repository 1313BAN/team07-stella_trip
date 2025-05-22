<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  title?: string;
  showTitle?: boolean;
  gap?: string | number;
  minItemWidth?: string;
  spacerCount?: number;
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  showTitle: true,
  gap: '1rem',
  minItemWidth: '14rem',
  spacerCount: 6,
});

// gap 값을 클래스로 변환
const gapValue = computed(() => {
  if (typeof props.gap === 'number') {
    return `${props.gap}px`;
  }
  return props.gap;
});

// 자리 채우기용 div 배열 생성
const spacers = computed(() => Array(props.spacerCount).fill(null));
</script>

<template>
  <div class="w-full px-4">
    <!-- 타이틀 (옵션) -->
    <h2 v-if="showTitle && title" class="text-2xl font-bold text-white">{{ title }}</h2>

    <!-- Flex 컨테이너 -->
    <div class="flex w-full flex-wrap pt-4" :style="{ gap: gapValue }">
      <slot></slot>

      <!-- 자리 채우기용 div -->
      <div
        v-for="(_, index) in spacers"
        :key="index"
        class="invisible flex-1"
        :style="{ minWidth: minItemWidth }"
      ></div>
    </div>
  </div>
</template>

<style scoped>
.flex > * {
  flex: 1 1 0 !important;
}
</style>
