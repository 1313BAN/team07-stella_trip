<script setup lang="ts">
interface Props {
  title: string;
}

defineProps<Props>();
</script>

<template>
  <div class="grid-card-container">
    <h2 class="mb-4 text-2xl font-bold text-white">{{ title }}</h2>

    <!-- Flex 컨테이너 -->
    <div class="flex w-full flex-wrap gap-4">
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.grid-card-container {
  width: 100%;
  padding: 1rem 0;
}

/* 슬롯에 들어가는 직계 자식 요소들 */
:deep(.flex > *) {
  flex: 1 1 0 !important;
  /* 
    - flex-grow: 1 → 공간이 남으면 균등하게 확장 (min-width 보다 크게)
    - flex-shrink: 1 → 공간이 부족하면 균등하게 축소 (min-width까지만)
    - flex-basis: 0 → 균등한 너비 배분의 시작점
  */

  /* 기존 min-width와 max-width는 유지됨 */
}
</style>
