<template>
  <div class="flex items-center justify-between px-4">
    <div class="flex cursor-pointer items-center gap-1" role="button" tabindex="0">
      <h3 class="text-2xl font-bold text-white">{{ title }}</h3>
      <ChevronRight class="h-5 w-5 text-purple-400" />
    </div>
  </div>
  <div class="h-98 px-4 pt-4 pb-8">
    <ErrorContent
      :error="error"
      :resetError="resetError"
      :showRetry="true"
      :title="errorTitle || title + ' 오류'"
      :description="errorDescription || title + ' 정보를 불러오는 중 문제가 발생했습니다'"
    />
  </div>
</template>

<script setup lang="ts">
import ErrorContent from '@/components/common/ErrorContent/ErrorContent.vue';
import { ChevronRight } from 'lucide-vue-next';

interface ErrorProps {
  error: Error | null;
  resetError: () => void;
  title: string;
  errorTitle?: string;
  errorDescription?: string;
}

defineProps<ErrorProps>();
</script>
