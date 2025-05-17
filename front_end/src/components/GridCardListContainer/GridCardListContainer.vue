<script setup lang="ts">
interface Props {
  title: string;
}

defineProps<Props>();
</script>

<template>
  <div class="w-full px-4">
    <h2 class="text-2xl font-bold text-white">{{ title }}</h2>

    <!-- Flex 컨테이너 -->
    <div class="flex w-full flex-wrap gap-4 pt-4">
      <slot></slot>
      <div class="invisible min-w-72 flex-1 p-4"></div>
      <div class="invisible min-w-72 flex-1 p-4"></div>
    </div>
  </div>
</template>

<style scoped>
:deep(.flex > *) {
  flex: 1 1 0 !important;
}
</style>
