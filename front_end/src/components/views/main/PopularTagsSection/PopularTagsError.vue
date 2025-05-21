<template>
  <div class="mb-8">
    <h2 class="mb-4 text-2xl font-bold text-white">인기 여행 태그</h2>
    <ErrorContent
      :error="error"
      :resetError="resetError"
      :showRetry="true"
      title="인기 여행 태그"
      description="태그를 불러오는 중 문제가 발생했습니다"
    />
  </div>
</template>

<script setup lang="ts">
import ErrorContent, { type ErrorProps } from '@/components/common/ErrorContent/ErrorContent.vue';

defineProps<ErrorProps>();
</script>
