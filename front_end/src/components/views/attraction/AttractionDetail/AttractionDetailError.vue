<template>
  <ErrorContent
    :error="error"
    :resetError="resetError"
    :showRetry="true"
    :title="title"
    :description="description || `${attractionName}의 정보를 불러오는 중 문제가 발생했습니다`"
  />
</template>

<script setup lang="ts">
import ErrorContent, { type ErrorProps } from '@/components/common/ErrorContent/ErrorContent.vue';

interface AttractionDetailErrorProps extends ErrorProps {
  attractionName: string;
}

defineProps<AttractionDetailErrorProps>();
</script>
