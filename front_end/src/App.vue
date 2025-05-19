<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue';
import GlobalErrorBoundary from '@/components/global/GlobalErrorBoundary.vue';
import GlobalFallback from '@/components/global/GlobalFallback.vue';

const hasError = ref(false);

onErrorCaptured(e => {
  hasError.value = true;
  console.log(e);

  return false;
});
</script>

<template>
  <GlobalErrorBoundary v-if="hasError" />
  <Suspense v-else>
    <template #default>
      <RouterView />
    </template>
    <template #fallback>
      <GlobalFallback />
    </template>
  </Suspense>
</template>
