<script setup lang="ts">
import { Badge } from '@/components/ui/badge';

interface Props {
  label: string;
  isOverflow?: boolean;
}

defineProps<Props>();
</script>

<template>
  <Badge
    class="rounded-full border-0 px-3 py-1.5 text-xs"
    :class="isOverflow ? 'bg-gray-600/30 text-gray-300' : 'bg-purple-500/30 text-purple-200'"
  >
    {{ label }}
  </Badge>
</template>
