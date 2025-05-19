<template>
  <div
    class="flex h-full w-full items-center justify-center rounded-lg border border-purple-400/30 bg-slate-800 p-6 text-white"
  >
    <div class="flex items-start gap-3">
      <div class="rounded-full bg-red-500/30 p-1.5">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="text-red-300"
        >
          <circle cx="12" cy="12" r="10" />
          <line x1="12" y1="8" x2="12" y2="12" />
          <line x1="12" y1="16" x2="12.01" y2="16" />
        </svg>
      </div>
      <div class="flex-1 space-y-2">
        <h3 v-if="title" class="text-lg font-semibold text-white">{{ title }}</h3>
        <p class="text-gray-200">{{ description || '문제가 발생했습니다' }}</p>
        <button
          v-if="showRetry"
          class="mt-3 inline-flex items-center gap-1.5 rounded bg-purple-600 px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-purple-700"
          @click="resetError"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M21 12a9 9 0 0 0-9-9 9.75 9.75 0 0 0-6.74 2.74L3 8" />
            <path d="M3 3v5h5" />
            <path d="M3 12a9 9 0 0 0 9 9 9.75 9.75 0 0 0 6.74-2.74L21 16" />
            <path d="M16 21h5v-5" />
          </svg>
          다시 시도
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface ErrorProps {
  error: Error | null;
  resetError: () => void;
  title?: string;
  description?: string;
  showRetry?: boolean;
}

defineProps<ErrorProps>();
</script>
