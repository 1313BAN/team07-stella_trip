<template>
  <div class="flex gap-3 rounded-lg p-2 transition-colors hover:bg-white/5">
    <div
      class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-purple-900/50 text-purple-200"
    >
      {{ order }}
    </div>

    <div class="flex-1">
      <div class="mb-1 flex items-center justify-between">
        <h5 class="font-medium text-white">{{ name }}</h5>
        <span class="text-sm text-purple-300">
          {{ formattedTime }}
        </span>
      </div>

      <div class="flex items-start gap-1 text-xs text-gray-400">
        <MapPin class="mt-0.5 h-3 w-3 flex-shrink-0 text-purple-400" />
        <span class="line-clamp-1">{{ address }}</span>
      </div>

      <div v-if="memo" class="mt-1 text-sm text-gray-300">
        <span class="line-clamp-2">{{ memo }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { MapPin } from 'lucide-vue-next';

const props = defineProps<{
  order: number;
  name: string;
  visitTime?: string;
  address: string;
  memo?: string;
}>();

const formattedTime = computed(() => {
  if (!props.visitTime) return '';
  const [hours, minutes] = props.visitTime.split(':');
  return `${hours}:${minutes}`;
});
</script>
