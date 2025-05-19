<script setup lang="ts">
import { ref, onErrorCaptured, type Component } from 'vue';

interface Props {
  loadingComponent: Component;
  errorComponent: Component;
}

defineProps<Props>();

const error = ref<Error | null>(null);
const resetError = () => {
  error.value = null;
};

onErrorCaptured((err, instance, info) => {
  error.value = err;
  console.error('[AsyncContainer]', err, info);

  return false;
});
</script>

<template>
  <component :is="errorComponent" v-if="error" :error="error" :resetError="resetError" />
  <Suspense v-else>
    <template #default>
      <slot />
    </template>
    <template #fallback>
      <component :is="loadingComponent" />
    </template>
  </Suspense>
</template>
