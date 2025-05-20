<template>
  <div class="mb-8">
    <h2 class="mb-4 text-2xl font-bold">인기 여행 태그</h2>
    <div class="rounded-md border border-red-200 bg-red-50 p-4">
      <p class="text-red-800">태그를 불러오는 중 문제가 발생했습니다</p>
      <button
        class="mt-2 rounded bg-red-100 px-4 py-1 text-red-800 hover:bg-red-200"
        @click="resetError"
      >
        다시 시도
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
interface ErrorProps {
  error: Error | null;
  resetError: () => void;
}

defineProps<ErrorProps>();
</script>
