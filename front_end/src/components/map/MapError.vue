<template>
  <div class="h-full w-full p-4">
    <ErrorContent
      :error="error"
      :resetError="resetError"
      :showRetry="true"
      :title="errorTitle || title + ' 오류'"
      :description="errorDescription || '지도를 불러오는 중 문제가 발생했습니다'"
    />
  </div>
</template>

<script setup lang="ts">
import ErrorContent from '@/components/common/ErrorContent/ErrorContent.vue';

interface MapErrorProps {
  error: Error | null;
  resetError: () => void;
  title: string;
  errorTitle?: string;
  errorDescription?: string;
}

defineProps<MapErrorProps>();
</script>
