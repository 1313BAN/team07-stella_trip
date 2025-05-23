<script setup lang="ts">
const emit = defineEmits<{
  addPlanClick: [];
}>();

const handleClick = () => {
  emit('addPlanClick');
};
</script>

<template>
  <div
    class="flex w-full max-w-72 min-w-56 cursor-pointer items-center justify-center rounded-2xl border border-dashed border-white/30 bg-white/10 text-white backdrop-blur-md transition-all duration-300 hover:scale-105 hover:border-purple-400/50 hover:bg-white/20 hover:shadow-xl hover:shadow-purple-500/25"
    role="button"
    tabindex="0"
    aria-label="여행 계획 추가하기"
    @click="handleClick"
  >
    <div class="flex flex-col items-center gap-2">
      <span class="text-lg font-semibold">여행계획 추가</span>
      <span class="text-2xl font-bold">+</span>
    </div>
  </div>
</template>
