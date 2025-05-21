<template>
  <ErrorContent
    :error="error"
    :resetError="resetError"
    :showRetry="true"
    :description="description || '지도를 불러오는 중 문제가 발생했습니다'"
  />
</template>

<script setup lang="ts">
import ErrorContent, { type ErrorProps } from '@/components/common/ErrorContent/ErrorContent.vue';

defineProps<ErrorProps>();
</script>
