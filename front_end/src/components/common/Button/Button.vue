<script setup lang="ts">
import { Button } from '@/components/ui/button';

// eslint-disable-next-line @typescript-eslint/no-empty-object-type
export interface ButtonInterface {
  //TODO: 버튼 커스텀 속성
}

const emit = defineEmits<{
  click: [event: MouseEvent];
}>();

const handleClick = (event: MouseEvent) => {
  emit('click', event);
};
</script>

<template>
  <Button v-bind="$attrs" @click="handleClick">
    <slot />
  </Button>
</template>
