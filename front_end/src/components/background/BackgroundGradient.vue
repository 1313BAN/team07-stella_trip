<script setup lang="ts">
import { computed } from 'vue';
import type { BackgroundGradientProps } from './types';

const props = withDefaults(defineProps<BackgroundGradientProps>(), {
  from: 'from-slate-950',
  via: 'via-purple-950',
  to: 'to-slate-900',
  direction: 'to-br',
});

const directionClasses = {
  'to-br': 'bg-gradient-to-br',
  'to-bl': 'bg-gradient-to-bl',
  'to-tr': 'bg-gradient-to-tr',
  'to-tl': 'bg-gradient-to-tl',
  'to-b': 'bg-gradient-to-b',
  'to-t': 'bg-gradient-to-t',
  'to-l': 'bg-gradient-to-l',
  'to-r': 'bg-gradient-to-r',
} as const;

const backgroundClasses = computed(() =>
  ['absolute inset-0', directionClasses[props.direction], props.from, props.via, props.to].join(' ')
);
</script>

<template>
  <div :class="backgroundClasses"></div>
</template>
