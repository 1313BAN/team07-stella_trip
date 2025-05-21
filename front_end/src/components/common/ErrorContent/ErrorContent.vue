<template>
  <div
    class="flex h-full w-full items-center justify-center rounded-lg border border-purple-400/30 bg-slate-800/75 p-6 text-white"
  >
    <div class="flex items-start gap-3">
      <div class="rounded-full bg-red-500/30 p-1.5">
        <AlertCircle :size="16" class="text-red-300" />
      </div>
      <div class="flex-1 space-y-2">
        <h3 v-if="title" class="text-lg font-semibold text-white">{{ title }}</h3>
        <p class="text-gray-200">
          {{ description || (error ? error.message : '문제가 발생했습니다') }}
        </p>
        <button
          v-if="showRetry"
          class="mt-3 inline-flex items-center gap-1.5 rounded bg-purple-600 px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-purple-700"
          @click="resetError"
        >
          <RotateCcw :size="14" />
          다시 시도
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { AlertCircle, RotateCcw } from 'lucide-vue-next';

export interface ErrorProps {
  error: Error | null;
  resetError: () => void;
  title?: string;
  description?: string;
  showRetry?: boolean;
}

defineProps<ErrorProps>();
</script>
