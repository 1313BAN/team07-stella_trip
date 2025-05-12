<script setup lang="ts">
import { Button } from '@/components/ui/button';

// eslint-disable-next-line @typescript-eslint/no-empty-object-type
export interface ButtonInterface {
  //TODO: 버튼 커스텀 속성
}

defineEmits<{
  click: [event: MouseEvent];
}>();
</script>

<template>
  <Button v-bind="$attrs" @click="$emit('click', $event)">
    <slot />
  </Button>
</template>
