<script setup lang="ts">
import { Badge } from '@/components/ui/badge';

interface Props {
  label: string;
  isOverflow?: boolean;
}

defineProps<Props>();

const emit = defineEmits<{
  tagClick: [];
}>();

const handleClick = () => {
  emit('tagClick');
};
</script>

<template>
  <Badge
    role="button"
    tabindex="0"
    class="cursor-pointer rounded-full border-0 px-3 py-1.5 text-xs transition-colors hover:opacity-80"
    :class="isOverflow ? 'bg-gray-600/30 text-gray-300' : 'bg-purple-500/30 text-purple-200'"
    @click.stop="handleClick"
    @keydown.enter="handleClick"
  >
    {{ label }}
  </Badge>
</template>
